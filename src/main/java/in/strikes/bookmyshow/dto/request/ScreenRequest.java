package in.strikes.bookmyshow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ScreenRequest(
        @NotBlank(message = "Name is required") String name,
        @Positive(message = "Total seats must be positive") Integer totalSeats,
        @NotNull(message = "Theatre ID is required") Long theatreId
) {
}
