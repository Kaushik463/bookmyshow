package in.strikes.bookmyshow.service.impl;

import in.strikes.bookmyshow.dto.request.LoginRequest;
import in.strikes.bookmyshow.dto.request.RegisterRequest;
import in.strikes.bookmyshow.dto.response.AuthResponse;
import in.strikes.bookmyshow.entity.User;
import in.strikes.bookmyshow.enums.Role;
import in.strikes.bookmyshow.exception.ResourceConflictException;
import in.strikes.bookmyshow.repository.UserRepository;
import in.strikes.bookmyshow.security.JwtService;
import in.strikes.bookmyshow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceConflictException("Email already registered: " + request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(toUserDetails(user));

        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication"));

        String token = jwtService.generateToken(toUserDetails(user));

        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole().name());
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }
}
