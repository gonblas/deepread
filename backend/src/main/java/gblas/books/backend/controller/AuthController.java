package gblas.books.backend.controller;

import gblas.books.backend.dto.*;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.mapper.UserMapper;
import gblas.books.backend.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@Validated
@RequestMapping("/api/auth")
@Tag(name = "User Management", description = "APIs for managing users")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping(path = "/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping(path = "/me")
    public UserResponse me(@AuthenticationPrincipal UserEntity user) {
        return UserMapper.INSTANCE.toDto(user);
    }

    @DeleteMapping(path = "/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @RequestBody DeleteAccountRequest request, @AuthenticationPrincipal UserEntity user) {
        authService.deleteAccount(request, user);
    }
}

