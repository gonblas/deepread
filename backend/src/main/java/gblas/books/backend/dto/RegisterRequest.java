package gblas.books.backend.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterRequest(@NotNull String email, @NotNull String password, @NotNull String username) {
}
