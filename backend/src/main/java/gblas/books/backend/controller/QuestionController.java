package gblas.books.backend.controller;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.service.question.QuestionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/{quizId}/questions")
@AllArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponse createQuestion(@Valid @PathVariable UUID quizId, @Valid @RequestBody QuestionRequest questionRequest) {
        return questionService.addQuestion(quizId, questionRequest);
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@Valid @PathVariable UUID quizId, @Valid @PathVariable UUID questionId) {
        questionService.deleteQuestion(quizId, questionId);
    }

//    @PutMapping("/{questionId}")
//    public QuestionResponse changeQuestion(@Valid @PathVariable UUID quizId, @Valid @PathVariable UUID questionId, @Valid @RequestBody QuestionRequest questionRequest) {
//        return questionService.changeQuestion(quizId, questionId, questionRequest);
//    }

//    @PatchMapping("/{questionId}")
//    public QuestionResponse updateQuestion(@Valid @PathVariable UUID quizId, @Valid @PathVariable UUID questionId, @Valid @RequestBody UpdateQuestionRequest questionRequest) {
//        return questionService.updateQuestion(quizId, questionId, questionRequest);
//    }
}
