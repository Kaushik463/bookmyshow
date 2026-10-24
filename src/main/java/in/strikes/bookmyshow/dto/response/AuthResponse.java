package in.strikes.bookmyshow.dto.response;

public record AuthResponse(
        String token,
        String email,
        String name,
        String role
) { }
