package in.strikes.bookmyshow.service;

import in.strikes.bookmyshow.dto.request.BookingRequest;
import in.strikes.bookmyshow.dto.request.PaymentRequest;
import in.strikes.bookmyshow.dto.response.BookingResponse;
import in.strikes.bookmyshow.dto.response.SeatAvailabilityResponse;

import java.util.List;

public interface BookingService {
    BookingResponse holdSeats(BookingRequest request, String userEmail);
    BookingResponse confirmBooking(Long bookingId, PaymentRequest paymentRequest, String userEmail);
    void cancelBooking(Long bookingId, String userEmail);
    List<SeatAvailabilityResponse> getSeatAvailability(Long showId);
    void expireStaleBookings();
}
