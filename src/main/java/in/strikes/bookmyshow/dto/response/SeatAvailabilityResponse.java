package in.strikes.bookmyshow.dto.response;

import in.strikes.bookmyshow.enums.SeatStatus;
import in.strikes.bookmyshow.enums.SeatType;

import java.math.BigDecimal;

public record SeatAvailabilityResponse(
        Long showSeatId,
        Long seatId,
        String rowLabel,
        Integer seatNumber,
        SeatType seatType,
        SeatStatus status,
        BigDecimal price
) {
}
