package gblas.books.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gblas.books.backend.validation.ValidEmail;
import gblas.books.backend.validation.ValidPassword;

@JsonIgnoreProperties(ignoreUnknown = false)
public record LoginRequest(
        @ValidEmail String email,
        @ValidPassword String password
) {}