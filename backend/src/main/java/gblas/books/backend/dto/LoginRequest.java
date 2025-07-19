package gblas.books.backend.dto;

import gblas.books.backend.validation.ValidEmail;
import gblas.books.backend.validation.ValidPassword;

public record LoginRequest(
        @ValidEmail String email,
        @ValidPassword String password
) {}