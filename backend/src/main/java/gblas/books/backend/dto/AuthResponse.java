package gblas.books.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponse(@NotBlank String token) {}
