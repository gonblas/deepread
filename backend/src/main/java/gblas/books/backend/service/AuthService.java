package gblas.books.backend.service;

import gblas.books.backend.dto.AuthResponse;
import gblas.books.backend.dto.LoginRequest;
import gblas.books.backend.dto.RegisterRequest;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
        log.info("Registrando usuario: {}", request);
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
}

