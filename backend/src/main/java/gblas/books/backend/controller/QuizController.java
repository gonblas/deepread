package gblas.books.backend.controller;

import gblas.books.backend.dto.QuizRequest;
import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Tag(name = "Quiz Management", description = "APIs for managing quizzes")
public class QuizController {
    private QuizService quizService;

    @Operation(
            summary = "Get paginated quizzes of authenticated user",
            description = "Retrieves paginated quizzes associated with the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no response body", content = @Content)
            }
    )
    @GetMapping("/api/quizzes")
    public Page<QuizResponse> getQuizzesFromUser(@AuthenticationPrincipal UserEntity user, Pageable pageable) {
        return quizService.getAllQuizzesFromUser(user, pageable);
    }

    @Operation(
            summary = "Get paginated quizzes for a book",
            description = "Retrieves paginated quizzes related to a specific book.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book not found - no response body", content = @Content)
            }
    )
    @GetMapping("/api/books/{bookId}/quizzes")
    public Page<QuizResponse> getQuizzesFromBook(@Valid @PathVariable UUID bookId, Pageable pageable) {
        return quizService.getAllQuizzesFromBook(bookId, pageable);
    }

    @Operation(
            summary = "Get quiz from a chapter",
            description = "Retrieves the quiz associated with a specific chapter of a book.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book or chapter ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book or chapter, Quiz not found or Chapter does not belong to this book - no response body", content = @Content)
            }
    )
    @GetMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
    public QuizResponse getQuizFromChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
        return quizService.getQuizFromChapter(bookId, chapterId);
    }

    @Operation(
            summary = "Create a quiz for a chapter",
            description = "Creates a new quiz associated with the specified chapter of a book.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quiz created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid data or IDs - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book or chapter not found - no response body", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Chapter Already Having Quiz - no response body", content = @Content)
            }
    )
    @PostMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizResponse createQuiz(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody QuizRequest quizRequest) {
        return quizService.addQuiz(bookId, chapterId, quizRequest);
    }

    @Operation(
            summary = "Delete a quiz for a chapter",
            description = "Deletes the quiz associated with the specified chapter of a book.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Quiz deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book or chapter ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book or chapter or quiz not found - no response body", content = @Content)
            }
    )
    @DeleteMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
        quizService.deleteQuiz(bookId, chapterId);
    }
}
