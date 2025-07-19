package gblas.books.backend.service;

import gblas.books.backend.dto.*;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.exceptions.UserAlreadyExistsException;
import gblas.books.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        user.setHashedPassword(passwordEncoder.encode(request.password()));
        user.setUsername(request.username());
        if(userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email " + request.email() + " already exists.");
        }
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

    public MeResponse deleteAccount(DeleteAccountRequest request, Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email).orElseThrow();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password())
        );

        userRepository.delete(user);
        return new MeResponse(user.getUsername());
    }
}

