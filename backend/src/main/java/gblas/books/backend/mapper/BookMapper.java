package gblas.books.backend.mapper;

import gblas.books.backend.dto.BookResponse;
import gblas.books.backend.entity.BookEntity;

import java.util.ArrayList;

public class BookMapper {
    public static BookResponse dtoFrom(BookEntity book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getGenre(),
                new ArrayList<>(book.getAuthors())
        );
    } 
}
