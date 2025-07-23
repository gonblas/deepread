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
    public ResponseEntity<QuizResponse> getQuizFromChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
        QuizResponse quiz = quizService.getQuizFromChapter(bookId, chapterId);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
    public ResponseEntity<QuizResponse> createQuiz(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody QuizRequest quizRequest) {
        QuizResponse quiz = quizService.addQuiz(bookId, chapterId, quizRequest);
        return ResponseEntity.ok(quiz);
    }

    @DeleteMapping("/api/books/{bookId}/chapters/{chapterId}/quiz")
    public ResponseEntity<?> deleteChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
        quizService.deleteQuiz(bookId, chapterId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/quiz/test")
    public void pushQuizzes(@AuthenticationPrincipal UserEntity user) {
        quizRepository.deleteAll();
        Iterable<ChapterEntity> chapters = chapterRepository.findAll();

        ChapterEntity firstChapter = chapters.iterator().hasNext()
                ? chapters.iterator().next()
                : null;

        QuizEntity quiz = new QuizEntity();
        quiz.setChapter(firstChapter); // ya persistido
        quiz.setQuestions(new ArrayList<>()); // redundante, pero claro

//        OpenQuestionEntity q1 = new OpenQuestionEntity();
//        q1.setType(QuestionEntity.QuestionType.OPEN);
//        q1.setPrompt("Enter open question");
//        q1.setExpectedAnswer("nashe");
//
//        TrueOrFalseQuestionEntity q2 = new TrueOrFalseQuestionEntity();
//        q2.setType(QuestionEntity.QuestionType.TRUE_FALSE);
//        q2.setPrompt("Is it nashe?");
//        q2.setIsAnswerTrue(true);
//
//        // Asociá desde el lado del "dueño"
//        quiz.getQuestions().add(q1);
//        quiz.getQuestions().add(q2);

        // Hibernate se encarga de asignar el quiz a cada pregunta
//        q1.setQuiz(quiz);
//        q2.setQuiz(quiz);

        quizRepository.save(quiz); // <-- solo este save

    }
}