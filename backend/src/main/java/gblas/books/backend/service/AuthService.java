package gblas.books.backend.service;

import gblas.books.backend.dto.*;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.exceptions.UserAlreadyExistsException;
import gblas.books.backend.mapper.UserMapper;
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
        if(userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email " + request.email() + " already exists.");
        }
        UserEntity user = UserMapper.INSTANCE.toEntity(request, passwordEncoder);
        log.info("Registering user with password {}", user.getHashedPassword());
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

    public void deleteAccount(DeleteAccountRequest request, UserEntity user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), request.password())
        );

        userRepository.delete(user);
    }
}

