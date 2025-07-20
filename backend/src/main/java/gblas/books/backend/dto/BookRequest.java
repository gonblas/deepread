package gblas.books.backend.dto;

import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.validation.ValidTitle;

import java.util.List;

public record BookRequest(
        @ValidTitle String title,
        String description,
        BookEntity.BookGenre genre,
        List<String> authors
) { }
