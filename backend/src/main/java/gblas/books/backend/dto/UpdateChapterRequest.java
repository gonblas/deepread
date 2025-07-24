package gblas.books.backend.dto;

import gblas.books.backend.validation.NotBlankIfPresent;

public record UpdateChapterRequest(
        @NotBlankIfPresent String title,
        @NotBlankIfPresent Integer number,
        String summary
) { }