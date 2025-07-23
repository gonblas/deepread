package gblas.books.backend.dto;

import gblas.books.backend.validation.ValidEmail;
import gblas.books.backend.validation.ValidUsername;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserResponse(@NotBlank UUID id, @ValidEmail String email, @ValidUsername String username) {
}
