package gblas.books.backend.dto;

import gblas.books.backend.validation.ValidPassword;
import jakarta.validation.constraints.NotNull;

public record DeleteAccountRequest(@ValidPassword String password) { }
