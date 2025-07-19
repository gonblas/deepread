package gblas.books.backend.dto;

import gblas.books.backend.validation.ValidEmail;

public record MeResponse(@ValidEmail String email) {
}
