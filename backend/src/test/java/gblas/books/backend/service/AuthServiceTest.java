package gblas.books.backend.service;

import gblas.books.backend.dto.AuthResponse;
import gblas.books.backend.dto.DeleteAccountRequest;
import gblas.books.backend.dto.LoginRequest;
import gblas.books.backend.dto.RegisterRequest;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.exceptions.UserAlreadyExistsException;
import gblas.books.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @InjectMocks private AuthService underTest;

    @Test
    void register_shouldRegisterUserAndReturnToken_whenEmailDoesNotExist() {
        String email = "example@xyz.com";
        RegisterRequest registerRequest = new RegisterRequest(email, "secure_password", "mi_username");
        underTest.register(registerRequest);
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).encode("secure_password");
        verify(userRepository).save(any());
        verify(jwtService).generateToken(any());
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("example@xyz.com", "password123", "username");

        when(userRepository.findByEmail("example@xyz.com"))
                .thenReturn(Optional.of(new UserEntity()));

        UserAlreadyExistsException ex = assertThrows(
                UserAlreadyExistsException.class,
                () -> underTest.register(request)
        );

        assertEquals("Email example@xyz.com already exists.", ex.getMessage());

        verify(userRepository, never()).save(any());
    }


    @Test
    void login_shouldAuthenticateAndReturnToken() {
        String email = "example@xyz.com";
        String password = "secure_password";
        String token = "fake-jwt-token";

        LoginRequest loginRequest = new LoginRequest(email, password);

        UserEntity user = new UserEntity();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(email)).thenReturn(token);

        AuthResponse response = underTest.login(loginRequest);

        assertEquals(token, response.token());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        verify(userRepository).findByEmail(email);
        verify(jwtService).generateToken(email);
    }

    @Test
    void login_shouldThrow_whenCredentialsAreInvalid() {
        LoginRequest loginRequest = new LoginRequest("example@xyz.com", "wrong_password");

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any());

        assertThrows(BadCredentialsException.class, () -> underTest.login(loginRequest));

        verify(authenticationManager).authenticate(any());
        verify(userRepository, never()).findByEmail(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void deleteAccount_shouldAuthenticateAndDeleteUser() {
        String email = "example@xyz.com";
        String password = "secure_password";

        UserEntity user = new UserEntity();
        user.setEmail(email);

        DeleteAccountRequest request = new DeleteAccountRequest(password);

        underTest.deleteAccount(request, user);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        verify(userRepository).delete(user);
    }

    @Test
    void deleteAccount_shouldThrow_whenAuthenticationFails() {
        String email = "example@xyz.com";
        String wrongPassword = "wrong_password";

        UserEntity user = new UserEntity();
        user.setEmail(email);

        DeleteAccountRequest request = new DeleteAccountRequest(wrongPassword);

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any());

        assertThrows(BadCredentialsException.class, () -> underTest.deleteAccount(request, user));

        verify(authenticationManager).authenticate(any());
        verify(userRepository, never()).delete(any());
    }

}