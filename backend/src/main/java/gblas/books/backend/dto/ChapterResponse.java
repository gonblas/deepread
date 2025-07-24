package gblas.books.backend.dto;

import gblas.books.backend.validation.ValidChapterNumber;
import gblas.books.backend.validation.ValidTitle;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ChapterResponse(
        @NotBlank UUID id,
        @ValidTitle String title,
        @ValidChapterNumber int number,
        String summary
) { }
