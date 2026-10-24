package in.strikes.bookmyshow.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        int status,
        String message,
        LocalDateTime timestamp,
        List<String> details
) { }
