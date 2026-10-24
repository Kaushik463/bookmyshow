package in.strikes.bookmyshow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record MovieRequest(
        @NotBlank(message = "Title is required") String title,
        String genre,
        String language,
        @Positive(message = "Duration must be positive") Integer durationMins,
        LocalDate releaseDate,
        boolean active
) { }
