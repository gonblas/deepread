package gblas.books.backend.controller;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.service.QuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/{quizId}/questions")
@AllArgsConstructor
@Tag(name = "Question Management", description = "APIs for managing users")
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

    @PutMapping("/{questionId}")
    public QuestionResponse changeQuestion(@Valid @PathVariable UUID quizId, @Valid @PathVariable UUID questionId, @Valid @RequestBody QuestionRequest questionRequest) {
        return questionService.changeQuestion(quizId, questionId, questionRequest);
    }
}
