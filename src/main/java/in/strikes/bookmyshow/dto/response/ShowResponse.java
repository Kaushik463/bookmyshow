package in.strikes.bookmyshow.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShowResponse(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal basePrice,
        Long movieId,
        String movieTitle,
        Long screenId
) {
}
