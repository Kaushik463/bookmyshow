package in.strikes.bookmyshow.service.impl;

import in.strikes.bookmyshow.dto.request.BookingRequest;
import in.strikes.bookmyshow.entity.*;
import in.strikes.bookmyshow.enums.BookingStatus;
import in.strikes.bookmyshow.enums.SeatStatus;
import in.strikes.bookmyshow.exception.SeatUnavailableException;
import in.strikes.bookmyshow.repository.BookingRepository;
import in.strikes.bookmyshow.repository.ShowRepository;
import in.strikes.bookmyshow.repository.ShowSeatRepository;
import in.strikes.bookmyshow.repository.UserRepository;
import in.strikes.bookmyshow.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private ShowRepository showRepository;
    @Mock private ShowSeatRepository showSeatRepository;
    @Mock private UserRepository userRepository;
    @Mock private PaymentService paymentService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Show show;
    private Seat seat;
    private ShowSeat showSeat;

    @BeforeEach
    void setUp() {
        user = User.builder().name("Test User").email("test@test.com").build();

        Movie movie = Movie.builder().title("Inception").build();
        Screen screen = Screen.builder().name("Screen 1").build();
        show = Show.builder().movie(movie).screen(screen).basePrice(BigDecimal.valueOf(250)).build();
        show.setId(1L);

        seat = Seat.builder().rowLabel("A").seatNumber(1).build();
        seat.setId(1L);

        showSeat = ShowSeat.builder()
                .show(show)
                .seat(seat)
                .status(SeatStatus.AVAILABLE)
                .price(BigDecimal.valueOf(250))
                .build();
        showSeat.setId(1L);
    }

    @Test
    void holdSeats_shouldSucceed_whenSeatsAreAvailable() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(showSeatRepository.findAndLockByShowIdAndSeatIds(1L, List.of(1L)))
                .thenReturn(List.of(showSeat));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        BookingRequest request = new BookingRequest(1L, List.of(1L));

        var response = bookingService.holdSeats(request, "test@test.com");

        assertThat(response.status()).isEqualTo(BookingStatus.PENDING);
        assertThat(response.totalAmount()).isEqualByComparingTo(BigDecimal.valueOf(250));
        verify(showSeatRepository).saveAll(anyList());
    }

    @Test
    void holdSeats_shouldThrow_whenSeatAlreadyLocked() {
        showSeat.setStatus(SeatStatus.LOCKED);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(showSeatRepository.findAndLockByShowIdAndSeatIds(1L, List.of(1L)))
                .thenReturn(List.of(showSeat));

        BookingRequest request = new BookingRequest(1L, List.of(1L));

        assertThatThrownBy(() -> bookingService.holdSeats(request, "test@test.com"))
                .isInstanceOf(SeatUnavailableException.class)
                .hasMessageContaining("already taken or locked");

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void holdSeats_shouldThrow_whenSeatCountMismatch() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(showSeatRepository.findAndLockByShowIdAndSeatIds(1L, List.of(1L, 2L)))
                .thenReturn(List.of(showSeat));

        BookingRequest request = new BookingRequest(1L, List.of(1L, 2L));

        assertThatThrownBy(() -> bookingService.holdSeats(request, "test@test.com"))
                .hasMessageContaining("do not exist");
    }
}