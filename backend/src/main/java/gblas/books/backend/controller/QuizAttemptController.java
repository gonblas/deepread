package gblas.books.backend.controller;

import gblas.books.backend.dto.QuizAttemptRequest;
import gblas.books.backend.dto.QuizAttemptResponse;
import gblas.books.backend.entity.*;
import gblas.books.backend.service.QuizAttemptService;
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
public class QuizAttemptController {
    private QuizAttemptService quizAttemptService;

    @GetMapping("/api/quiz-attempts")
    public Page<QuizAttemptResponse> getQuizAttemptsFromUser(@AuthenticationPrincipal UserEntity user, Pageable pageable) {
        return quizAttemptService.getQuizAttemptsFromUser(user, pageable);
    }

    @GetMapping("/api/books/{bookId}/quiz-attempts")
    public Page<QuizAttemptResponse> getQuizAttemptsFromBook(@Valid @PathVariable UUID bookId, Pageable pageable) {
        return quizAttemptService.getQuizAttemptsFromBook(bookId, pageable);
    }

    @GetMapping("/api/books/{bookId}/chapters/{chapterId}/quiz-attempts")
    public Page<QuizAttemptResponse> getQuizAttemptFromChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, Pageable pageable) {
        return quizAttemptService.getQuizAttemptFromChapter(bookId, chapterId, pageable);
    }

    @PostMapping("/api/quizzes/{quizId}/quiz-attempts")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizAttemptResponse createQuizAttempt(@Valid @PathVariable UUID quizId, @Valid @RequestBody QuizAttemptRequest quizAttemptRequest) {
        log.info("Answer {}", quizAttemptRequest.answers().getFirst().toString());
        return quizAttemptService.createQuizAttempt(quizId, quizAttemptRequest);
    }

//    @DeleteMapping("/api/quiz-attempts/{quizId}/")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
//        quizAttemptService.deleteQuiz(bookId, chapterId);
//    }

}

