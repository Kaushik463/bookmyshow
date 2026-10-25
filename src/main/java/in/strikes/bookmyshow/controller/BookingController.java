package in.strikes.bookmyshow.controller;

import in.strikes.bookmyshow.dto.request.BookingRequest;
import in.strikes.bookmyshow.dto.request.PaymentRequest;
import in.strikes.bookmyshow.dto.response.BookingResponse;
import in.strikes.bookmyshow.dto.response.SeatAvailabilityResponse;
import in.strikes.bookmyshow.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/api/bookings")
    public ResponseEntity<BookingResponse> holdSeats(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.holdSeats(request, authentication.getName()));
    }

    @PostMapping("/api/bookings/{id}/pay")
    public ResponseEntity<BookingResponse> confirmBooking(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(bookingService.confirmBooking(id, request, authentication.getName()));
    }

    @DeleteMapping("/api/bookings/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication authentication) {
        bookingService.cancelBooking(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/shows/{showId}/seats")
    public ResponseEntity<List<SeatAvailabilityResponse>> getSeatAvailability(@PathVariable Long showId) {
        return ResponseEntity.ok(bookingService.getSeatAvailability(showId));
    }
}
