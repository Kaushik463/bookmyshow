package in.strikes.bookmyshow.service;

import in.strikes.bookmyshow.dto.request.LoginRequest;
import in.strikes.bookmyshow.dto.request.RegisterRequest;
import in.strikes.bookmyshow.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
