package gblas.books.backend.dto;

import gblas.books.backend.entity.BookEntity;

import java.util.List;
import java.util.UUID;

public record BookResponse(UUID id, String title, String description, BookEntity.BookGenre genre, List<String> authors) {
}
