package gblas.books.backend.controller;

import gblas.books.backend.dto.AuthResponse;
import gblas.books.backend.dto.LoginRequest;
import gblas.books.backend.dto.MeResponse;
import gblas.books.backend.dto.RegisterRequest;
import gblas.books.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication auth) {
        return ResponseEntity.ok(new MeResponse(auth.getName()));
    }
}

