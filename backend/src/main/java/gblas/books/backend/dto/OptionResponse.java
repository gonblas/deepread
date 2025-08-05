package gblas.books.backend.dto;

import java.util.UUID;

public record OptionResponse(
        UUID id,
        String text,
        Boolean isCorrect
) { }
