package gblas.books.backend.controller;

import gblas.books.backend.dto.*;
import gblas.books.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping(path = "/me", produces = "application/json")
    public ResponseEntity<MeResponse> me(Authentication auth) {
        return ResponseEntity.ok(new MeResponse(auth.getName()));
    }

    @DeleteMapping(path = "/delete", produces = "application/json")
    public ResponseEntity<MeResponse> delete(@Valid @RequestBody DeleteAccountRequest request, Authentication auth) {
        return ResponseEntity.ok(authService.deleteAccount(request, auth));
    }
}

