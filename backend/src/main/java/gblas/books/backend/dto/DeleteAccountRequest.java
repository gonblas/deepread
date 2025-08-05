package gblas.books.backend.dto;

import gblas.books.backend.validation.ValidPassword;

public record DeleteAccountRequest(@ValidPassword String password) { }
