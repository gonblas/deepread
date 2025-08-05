package gblas.books.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OptionRequest(
        @NotEmpty String text,
        @NotNull Boolean isCorrect
) { }