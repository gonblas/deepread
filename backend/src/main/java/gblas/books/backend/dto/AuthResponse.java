package gblas.books.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponse(@NotBlank String email, @NotBlank String username, @NotBlank String token) {}
