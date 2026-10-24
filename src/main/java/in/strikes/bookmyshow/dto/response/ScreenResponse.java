package in.strikes.bookmyshow.dto.response;

public record ScreenResponse(
        Long id,
        String name,
        Integer totalSeats,
        Long theatreId
) {
}
