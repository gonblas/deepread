package gblas.books.backend.dto;

import gblas.books.backend.validation.ValidEmail;
import gblas.books.backend.validation.ValidPassword;
import gblas.books.backend.validation.ValidUsername;

public record RegisterRequest(
        @ValidEmail String email,
        @ValidPassword String password,
        @ValidUsername String username
) { }
