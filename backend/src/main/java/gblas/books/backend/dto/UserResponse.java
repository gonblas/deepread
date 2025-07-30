package gblas.books.backend.dto;

import gblas.books.backend.validation.ValidEmail;
import gblas.books.backend.validation.ValidUsername;

public record UserResponse(@ValidEmail String email, @ValidUsername String username) {
}
