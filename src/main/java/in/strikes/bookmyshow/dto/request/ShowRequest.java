package in.strikes.bookmyshow.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShowRequest(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull @Positive BigDecimal basePrice,
        @NotNull Long movieId,
        @NotNull Long screenId
) {
}
