package in.strikes.bookmyshow.dto.response;

import java.time.LocalDate;

public record MovieResponse(
        Long id,
        String title,
        String genre,
        String language,
        Integer durationMins,
        LocalDate releaseDate,
        boolean active
) { }
