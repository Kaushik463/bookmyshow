package in.strikes.bookmyshow.scheduler;

import in.strikes.bookmyshow.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingExpiryScheduler {

    private final BookingService bookingService;

    @Scheduled(fixedRate = 60000) // every 60 seconds
    public void expireStaleBookings() {
        bookingService.expireStaleBookings();
    }
}
