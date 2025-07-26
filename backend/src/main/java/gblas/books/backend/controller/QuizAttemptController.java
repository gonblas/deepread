package gblas.books.backend.controller;

import gblas.books.backend.dto.QuizAttemptResponse;
import gblas.books.backend.dto.QuizRequest;
import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.entity.*;
import gblas.books.backend.service.QuizAttemptService;
import gblas.books.backend.service.QuizService;
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
public class QuizAttemptController {
    private QuizAttemptService quizAttemptService;

//    @GetMapping("/api/quiz-attempts")
//    public Page<QuizAttemptResponse> getQuizzesFromUser(@AuthenticationPrincipal UserEntity user, Pageable pageable) {
//        return quizAttemptService.getAllQuizAttemptsFromUser(user, pageable);
//    }

//    @GetMapping("/api/books/{bookId}/quiz-attempts")
//    public Page<QuizResponse> getQuizzesFromBook(@Valid @PathVariable UUID bookId, Pageable pageable) {
//        return quizAttemptService.getAllQuizzesFromBook(bookId, pageable);
//    }
//
//    @GetMapping("/api/books/{bookId}/chapters/{chapterId}/quiz-attempts")
//    public QuizResponse getQuizFromChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
//        return quizAttemptService.getQuizFromChapter(bookId, chapterId);
//    }
//
//    @PostMapping("/api/quizzes/{quizId}/quiz-attempts")
//    @ResponseStatus(HttpStatus.CREATED)
//    public QuizResponse createQuiz(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody QuizRequest quizRequest) {
//        return quizAttemptService.addQuiz(bookId, chapterId, quizRequest);
//    }
//
//    @DeleteMapping("/api/quiz-attempts/{quizId}/")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
//        quizAttemptService.deleteQuiz(bookId, chapterId);
//    }

}

