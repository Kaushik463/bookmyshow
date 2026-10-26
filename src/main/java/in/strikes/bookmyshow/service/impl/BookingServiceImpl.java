package in.strikes.bookmyshow.service.impl;

import in.strikes.bookmyshow.dto.request.BookingRequest;
import in.strikes.bookmyshow.dto.request.PaymentRequest;
import in.strikes.bookmyshow.dto.response.BookingResponse;
import in.strikes.bookmyshow.dto.response.SeatAvailabilityResponse;
import in.strikes.bookmyshow.entity.Booking;
import in.strikes.bookmyshow.entity.Show;
import in.strikes.bookmyshow.entity.ShowSeat;
import in.strikes.bookmyshow.entity.User;
import in.strikes.bookmyshow.enums.BookingStatus;
import in.strikes.bookmyshow.enums.SeatStatus;
import in.strikes.bookmyshow.exception.BookingExpiredException;
import in.strikes.bookmyshow.exception.PaymentFailedException;
import in.strikes.bookmyshow.exception.ResourceNotFoundException;
import in.strikes.bookmyshow.exception.SeatUnavailableException;
import in.strikes.bookmyshow.repository.BookingRepository;
import in.strikes.bookmyshow.repository.ShowRepository;
import in.strikes.bookmyshow.repository.ShowSeatRepository;
import in.strikes.bookmyshow.repository.UserRepository;
import in.strikes.bookmyshow.service.BookingService;
import in.strikes.bookmyshow.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    private static final int HOLD_MINUTES = 5;

    @Override
    @Transactional
    public BookingResponse holdSeats(BookingRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Show show = showRepository.findById(request.showId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + request.showId()));

        // Pessimistic lock: concurrent requests for the same seats queue here at the DB level
        List<ShowSeat> showSeats = showSeatRepository
                .findAndLockByShowIdAndSeatIds(show.getId(), request.seatIds());

        if (showSeats.size() != request.seatIds().size()) {
            throw new ResourceNotFoundException("One or more seats do not exist for this show");
        }

        boolean anyUnavailable = showSeats.stream()
                .anyMatch(ss -> ss.getStatus() != SeatStatus.AVAILABLE);
        if (anyUnavailable) {
            throw new SeatUnavailableException("One or more selected seats are already taken or locked");
        }

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(HOLD_MINUTES);
        BigDecimal totalAmount = showSeats.stream()
                .map(ShowSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Booking booking = Booking.builder()
                .user(user)
                .show(show)
                .status(BookingStatus.PENDING)
                .totalAmount(totalAmount)
                .expiresAt(expiresAt)
                .build();

        booking = bookingRepository.save(booking);

        Booking finalBooking = booking;
        showSeats.forEach(ss -> {
            ss.setStatus(SeatStatus.LOCKED);
            ss.setBooking(finalBooking);
            ss.setLockedUntil(expiresAt);
        });
        showSeatRepository.saveAll(showSeats);

        return toResponse(booking, showSeats);
    }

    @Override
    public Page<BookingResponse> getMyBookings(String userEmail, Pageable pageable) {
        return bookingRepository.findByUser_Email(userEmail, pageable)
                .map(booking -> {
                    List<ShowSeat> showSeats = showSeatRepository.findByShowId(booking.getShow().getId())
                            .stream()
                            .filter(ss -> ss.getBooking() != null && ss.getBooking().getId().equals(booking.getId()))
                            .toList();
                    return toResponse(booking, showSeats);
                });
    }

    @Override
    @Transactional
    public BookingResponse confirmBooking(Long bookingId, PaymentRequest paymentRequest, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new ResourceNotFoundException("Booking not found: " + bookingId);
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingExpiredException("Booking is not in a payable state: " + booking.getStatus());
        }

        if (booking.getExpiresAt().isBefore(LocalDateTime.now())) {
            releaseSeatsAndExpire(booking);
            throw new BookingExpiredException("Booking hold has expired. Please select seats again.");
        }

        try {
            paymentService.processPayment(booking, paymentRequest);
        } catch (PaymentFailedException ex) {
            releaseSeatsAndExpire(booking);
            throw ex;
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        List<ShowSeat> showSeats = showSeatRepository.findByShowId(booking.getShow().getId())
                .stream().filter(ss -> ss.getBooking() != null && ss.getBooking().getId().equals(booking.getId()))
                .toList();
        showSeats.forEach(ss -> ss.setStatus(SeatStatus.BOOKED));
        showSeatRepository.saveAll(showSeats);

        bookingRepository.save(booking);

        return toResponse(booking, showSeats);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new ResourceNotFoundException("Booking not found: " + bookingId);
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return;
        }

        releaseSeats(booking);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public List<SeatAvailabilityResponse> getSeatAvailability(Long showId) {
        return showSeatRepository.findByShowId(showId).stream()
                .map(ss -> new SeatAvailabilityResponse(
                        ss.getId(),
                        ss.getSeat().getId(),
                        ss.getSeat().getRowLabel(),
                        ss.getSeat().getSeatNumber(),
                        ss.getSeat().getSeatType(),
                        ss.getStatus(),
                        ss.getPrice()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void expireStaleBookings() {
        List<Booking> staleBookings = bookingRepository
                .findByStatusAndExpiresAtBefore(BookingStatus.PENDING, LocalDateTime.now());

        for (Booking booking : staleBookings) {
            releaseSeats(booking);
            booking.setStatus(BookingStatus.EXPIRED);
        }
        bookingRepository.saveAll(staleBookings);
    }

    private void releaseSeatsAndExpire(Booking booking) {
        releaseSeats(booking);
        booking.setStatus(BookingStatus.EXPIRED);
        bookingRepository.save(booking);
    }

    private void releaseSeats(Booking booking) {
        List<ShowSeat> showSeats = showSeatRepository.findByShowId(booking.getShow().getId())
                .stream().filter(ss -> ss.getBooking() != null && ss.getBooking().getId().equals(booking.getId()))
                .toList();
        showSeats.forEach(ss -> {
            ss.setStatus(SeatStatus.AVAILABLE);
            ss.setBooking(null);
            ss.setLockedUntil(null);
        });
        showSeatRepository.saveAll(showSeats);
    }

    private BookingResponse toResponse(Booking booking, List<ShowSeat> showSeats) {
        return new BookingResponse(
                booking.getId(),
                booking.getStatus(),
                booking.getTotalAmount(),
                booking.getExpiresAt(),
                booking.getShow().getId(),
                showSeats.stream().map(ss -> ss.getSeat().getId()).toList()
        );
    }
}
