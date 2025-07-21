package gblas.books.backend.controller;

import gblas.books.backend.dto.ChapterRequest;
import gblas.books.backend.dto.ChapterResponse;
import gblas.books.backend.dto.QuizRequest;
import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.service.ChapterService;
import gblas.books.backend.service.QuizService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class QuizController {
    private QuizService quizService;

    @GetMapping("/api/quizzes")
    public ResponseEntity<Page<QuizResponse>> getQuizzesFromUser(Principal principal, Pageable pageable) {
        Page<QuizResponse> quizzes = quizService.getAllQuizzesFromUser(principal.getName(), pageable);
        return ResponseEntity.ok(quizzes);
    }

    @RequestMapping("/api/books/{bookId}/quizzes")
    public ResponseEntity<Page<QuizResponse>> getQuizzesFromBook(@Valid @PathVariable UUID bookId, Pageable pageable) {
        Page<QuizResponse> quizzes = quizService.getAllQuizzesFromBook(bookId, pageable);
        return ResponseEntity.ok(quizzes);
    }

    @RequestMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
    public ResponseEntity<Page<QuizResponse>> getQuizFromChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, Pageable pageable) {
        Page<QuizResponse> quizzes = quizService.getQuizFromChapter(bookId, chapterId, pageable);
        return ResponseEntity.ok(quizzes);
    }

//    @PostMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
//    public ResponseEntity<QuizResponse> createQuiz(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody QuizRequest quizRequest) {
//        QuizResponse quiz = quizService.addQuiz(bookId, chapterId, quizRequest);
//        return ResponseEntity.ok(quiz);
//    }
//
//    @DeleteMapping("/{chapterId}")
//    public ResponseEntity<?> deleteChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
//        quizService.deleteQuiz(bookId, chapterId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/{chapterId}")
//    public ResponseEntity<QuizResponse> updateChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody ChapterRequest chapterRequest) {
//        QuizResponse quiz = quizService.updateQuiz(bookId, chapterId, chapterRequest);
//        return ResponseEntity.ok(quiz);
//    }
}