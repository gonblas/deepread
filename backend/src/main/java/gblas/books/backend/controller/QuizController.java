package gblas.books.backend.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import gblas.books.backend.dto.ChapterRequest;
import gblas.books.backend.dto.ChapterResponse;
import gblas.books.backend.dto.QuizRequest;
import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.entity.*;
import gblas.books.backend.repository.ChapterRepository;
import gblas.books.backend.repository.QuizRepository;
import gblas.books.backend.service.ChapterService;
import gblas.books.backend.service.QuizService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class QuizController {
    private ChapterRepository chapterRepository;
    private QuizService quizService;
    private QuizRepository quizRepository;

    @GetMapping("/api/quizzes")
    public Page<QuizResponse> getQuizzesFromUser(@AuthenticationPrincipal UserEntity user, Pageable pageable) {
        return quizService.getAllQuizzesFromUser(user, pageable);
    }

    @GetMapping("/api/books/{bookId}/quizzes")
    public Page<QuizResponse> getQuizzesFromBook(@Valid @PathVariable UUID bookId, Pageable pageable) {
        return quizService.getAllQuizzesFromBook(bookId, pageable);
    }

    @GetMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
    public QuizResponse getQuizFromChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
        return quizService.getQuizFromChapter(bookId, chapterId);
    }

    @PostMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizResponse createQuiz(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody QuizRequest quizRequest) {
        return quizService.addQuiz(bookId, chapterId, quizRequest);
    }

    @DeleteMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
        quizService.deleteQuiz(bookId, chapterId);
    }

}