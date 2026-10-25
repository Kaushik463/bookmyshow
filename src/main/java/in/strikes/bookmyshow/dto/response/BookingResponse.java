package in.strikes.bookmyshow.dto.response;

import in.strikes.bookmyshow.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(
        Long id,
        BookingStatus status,
        BigDecimal totalAmount,
        LocalDateTime expiresAt,
        Long showId,
        List<Long> seatIds
) {
}
