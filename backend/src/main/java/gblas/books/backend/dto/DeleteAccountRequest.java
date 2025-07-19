package gblas.books.backend.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteAccountRequest(@NotNull String password) { }
