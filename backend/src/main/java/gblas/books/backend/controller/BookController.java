package gblas.books.backend.controller;

import gblas.books.backend.dto.BookRequest;
import gblas.books.backend.dto.BookResponse;
import gblas.books.backend.dto.BookUpdateRequest;
import gblas.books.backend.entity.BookEntity.BookGenre;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.repository.BookRepository;
import gblas.books.backend.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
@Tag(name = "Book Management", description = "APIs for managing users")
public class BookController {
    private BookService bookService;

    @Operation(
            summary = "Get paginated list of books",
            description = "Retrieves a pageable list of books, optionally filtered by genres.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no response body", content = @Content)
            }
    )
    @GetMapping
    public Page<BookResponse> getBooks(
            @Valid @RequestParam(required = false) List<BookGenre> genres,
            @Valid @RequestParam(required = false) String search,
            @AuthenticationPrincipal UserEntity user,
            Pageable pageable
    ) {
        return bookService.getBooks(user, pageable, genres, search);
    }

    @Operation(
            summary = "Get book details",
            description = "Retrieves a book details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
            }
    )
    @GetMapping("/{bookId}")
    public BookResponse getBookDetails(@PathVariable UUID bookId) {
        return bookService.getBookDetails(bookId);
    }

    @Operation(
            summary = "Add a new book",
            description = "Creates a new book associated with the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book data - no response body", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no response body", content = @Content)
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse addBook(
            @Valid @RequestBody BookRequest bookRequest,
            @AuthenticationPrincipal UserEntity user
    ) {
        return bookService.addBook(user, bookRequest);
    }

    @Operation(
            summary = "Delete a book",
            description = "Deletes the book with the specified ID belonging to the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no response body", content = @Content)
            }
    )
    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(
            @PathVariable UUID bookId,
            @AuthenticationPrincipal UserEntity user
    ) {
        bookService.deleteBook(user, bookId);
    }

    @Operation(
            summary = "Replace an existing book",
            description = "Fully updates the book with the specified ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book data or ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no response body", content = @Content)
            }
    )
    @PutMapping("/{bookId}")
    public BookResponse changeBook(
            @PathVariable UUID bookId,
            @Valid @RequestBody BookRequest bookRequest,
            @AuthenticationPrincipal UserEntity user
    ) {
        return bookService.changeBook(user, bookId, bookRequest);
    }

    @Operation(
            summary = "Partially update a book",
            description = "Partially updates the book with the specified ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book partially updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book data or ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no response body", content = @Content)
            }
    )
    @PatchMapping("/{bookId}")
    public BookResponse updateBook(
            @PathVariable UUID bookId,
            @Valid @RequestBody BookUpdateRequest bookRequest,
            @AuthenticationPrincipal UserEntity user
    ) {
        return bookService.updateBook(user, bookId, bookRequest);
    }

}
