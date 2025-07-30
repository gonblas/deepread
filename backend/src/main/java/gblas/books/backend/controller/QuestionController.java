package gblas.books.backend.controller;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/{quizId}/questions")
@AllArgsConstructor
@Tag(name = "Question Management", description = "APIs for managing questions within quizzes")
public class QuestionController {
    private final QuestionService questionService;

    @Operation(
            summary = "Create a new question",
            description = "Adds a new question to the specified quiz.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Question created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid question data or quiz ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Quiz not found - no response body", content = @Content)
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponse createQuestion(@Valid @PathVariable UUID quizId, @Valid @RequestBody QuestionRequest questionRequest) {
        return questionService.addQuestion(quizId, questionRequest);
    }

    @Operation(
            summary = "Delete a question",
            description = "Deletes the specified question from the given quiz.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid quiz or question ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Quiz or question not found - no response body", content = @Content)
            }
    )
    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@Valid @PathVariable UUID quizId, @Valid @PathVariable UUID questionId) {
        questionService.deleteQuestion(quizId, questionId);
    }

    @Operation(
            summary = "Fully update a question",
            description = "Replaces the question data with the provided information.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Question updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid question data or IDs - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Quiz or question not found - no response body", content = @Content)
            }
    )
    @PutMapping("/{questionId}")
    public QuestionResponse changeQuestion(@Valid @PathVariable UUID quizId, @Valid @PathVariable UUID questionId, @Valid @RequestBody QuestionRequest questionRequest) {
        return questionService.changeQuestion(quizId, questionId, questionRequest);
    }
}
