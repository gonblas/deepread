package gblas.books.backend.mapper;

import gblas.books.backend.dto.BookResponse;
import gblas.books.backend.dto.ChapterResponse;
import gblas.books.backend.entity.ChapterEntity;

public class ChapterMapper {
    public static ChapterResponse dtoFrom(ChapterEntity chapter) {
        return new ChapterResponse(
                chapter.getId(),
                chapter.getTitle(),
                chapter.getNumber(),
                chapter.getSummary(),
                chapter.getBook().getId()
        );
    }
}
