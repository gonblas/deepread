package gblas.books.backend.controller;

import gblas.books.backend.dto.QuizAttemptRequest;
import gblas.books.backend.dto.QuizAttemptResponse;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.service.QuizAttemptService;
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

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "Quiz attempts Management", description = "APIs for managing quiz attempts")
public class QuizAttemptController {
    private QuizAttemptService quizAttemptService;

    @Operation(
            summary = "Get paginated quiz attempts of authenticated user",
            description = "Retrieves paginated quiz attempts associated with the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz attempts retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - no response body", content = @Content)
            }
    )
    @GetMapping("/api/quiz-attempts")
    public Page<QuizAttemptResponse> getQuizAttemptsFromUser(@AuthenticationPrincipal UserEntity user, Pageable pageable) {
        return quizAttemptService.getQuizAttemptsFromUser(user, pageable);
    }

    @Operation(
            summary = "Get paginated quiz attempts for a book",
            description = "Retrieves paginated quiz attempts related to a specific book.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz attempts retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book not found - no response body", content = @Content)
            }
    )
    @GetMapping("/api/books/{bookId}/quiz-attempts")
    public Page<QuizAttemptResponse> getQuizAttemptsFromBook(@Valid @PathVariable UUID bookId, Pageable pageable) {
        return quizAttemptService.getQuizAttemptsFromBook(bookId, pageable);
    }

    @Operation(
            summary = "Get paginated quiz attempts for a chapter",
            description = "Retrieves paginated quiz attempts related to a specific chapter of a book.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quiz attempts retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book or chapter ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book or chapter not found - no response body", content = @Content)
            }
    )
    @GetMapping("/api/books/{bookId}/chapters/{chapterId}/quiz-attempts")
    public Page<QuizAttemptResponse> getQuizAttemptFromChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, Pageable pageable) {
        return quizAttemptService.getQuizAttemptFromChapter(bookId, chapterId, pageable);
    }

    @Operation(
            summary = "Create a quiz attempt",
            description = "Creates a new quiz attempt for the specified quiz.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quiz attempt created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid quiz ID or quiz attempt data - no response body - Wrong question id", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Quiz not found - no response body", content = @Content)
            }
    )
    @PostMapping("/api/quizzes/{quizId}/quiz-attempts")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizAttemptResponse createQuizAttempt(@Valid @PathVariable UUID quizId, @Valid @RequestBody QuizAttemptRequest quizAttemptRequest) {
        return quizAttemptService.createQuizAttempt(quizId, quizAttemptRequest);
    }

    @Operation(
            summary = "Delete a quiz attempt",
            description = "Deletes the quiz attempt with the specified ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Quiz attempt deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid quiz attempt ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Quiz attempt not found - no response body", content = @Content)
            }
    )
    @DeleteMapping("/api/quiz-attempts/{quizAttemptId}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuizAttempt(@Valid @PathVariable UUID quizAttemptId) {
        quizAttemptService.deleteQuizAttempt(quizAttemptId);
    }
}
