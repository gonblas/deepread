package gblas.books.backend.controller;

import gblas.books.backend.dto.BookResponse;
import gblas.books.backend.dto.BookRequest;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.BookEntity.*;
import gblas.books.backend.entity.ChapterEntity;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.repository.BookRepository;
import gblas.books.backend.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/book")
@AllArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getBooks(@Valid @RequestParam(required = false) List<BookGenre> genres, @AuthenticationPrincipal UserEntity user, Pageable pageable) {
        Page<BookResponse> books = bookService.getBooks(user, pageable, genres);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse addBook(@Valid @RequestBody BookRequest bookRequest, @AuthenticationPrincipal UserEntity user) {
        return bookService.addBook(user, bookRequest);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable UUID bookId, @AuthenticationPrincipal UserEntity user) {
        bookService.deleteBook(user, bookId);
    }

    @PutMapping("/{bookId}")
    public BookResponse updateBook(@PathVariable UUID bookId, @Valid @RequestBody BookRequest bookRequest, @AuthenticationPrincipal UserEntity user) {
        return bookService.updateBook(user, bookId, bookRequest);
    }

    @PostMapping("/test")
    public void getBooks(@AuthenticationPrincipal UserEntity user) {
        bookRepository.deleteAll();
        BookEntity book = new BookEntity();
        book.setTitle("Titulo de libro con capitulos agregados");
        book.setOwner(user);
        ChapterEntity c1 = new ChapterEntity();
        c1.setTitle("Titulo del c1");
        c1.setBook(book);

        ChapterEntity c2 = new ChapterEntity();
        c2.setTitle("Titulo del c2");
        c2.setBook(book);

        book.getChapters().add(c1);
        book.getChapters().add(c2);

        bookRepository.save(book);
    }


}
