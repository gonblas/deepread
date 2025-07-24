package gblas.books.backend.dto;

import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.validation.NotBlankIfPresent;

import java.util.List;

public record BookUpdateRequest(
        @NotBlankIfPresent String title,
        String description,
        BookEntity.BookGenre genre,
        List<String> authors
) { }
