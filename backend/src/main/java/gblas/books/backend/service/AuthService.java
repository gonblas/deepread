package gblas.books.backend.service;

import gblas.books.backend.dto.*;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        UserEntity user = new UserEntity();
        user.setEmail(request.email());
        user.setHashed_password(passwordEncoder.encode(request.password()));
        user.setUsername(request.username());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserEntity user = userRepository.findByEmail(request.email()).orElseThrow();
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    public MeResponse deleteAccount(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        log.info("Delete account with email {}", email);
        log.info("Delete user {}", user.getUsername());

        userRepository.delete(user);
        return new MeResponse(user.getUsername());
    }
}

