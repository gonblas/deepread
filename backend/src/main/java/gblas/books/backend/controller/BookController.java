package gblas.books.backend.controller;

import gblas.books.backend.dto.BookResponse;
import gblas.books.backend.dto.BookRequest;
import gblas.books.backend.entity.BookEntity.*;
import gblas.books.backend.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/book")
@AllArgsConstructor
public class BookController {

    private BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getBooks(@Valid @RequestParam(required = false) List<BookGenre> genres, Principal principal, Pageable pageable) {
        Page<BookResponse> books = bookService.getBooks(principal.getName(), pageable, genres);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookRequest bookRequest, Principal principal) {
        BookResponse book = bookService.addBook(principal.getName(), bookRequest);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable UUID bookId, Principal principal) {
        bookService.deleteBook(principal.getName(), bookId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID bookId, @Valid @RequestBody BookRequest bookRequest, Principal principal) {
        BookResponse book = bookService.updateBook(principal.getName(), bookId, bookRequest);
        return ResponseEntity.ok(book);
    }
}
