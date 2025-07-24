package gblas.books.backend.dto;

import gblas.books.backend.validation.ValidChapterNumber;
import gblas.books.backend.validation.ValidTitle;

public record ChapterRequest(
        @ValidTitle String title,
        @ValidChapterNumber Integer number,
        String summary
        ) { }
