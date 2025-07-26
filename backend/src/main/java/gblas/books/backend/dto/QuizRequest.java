package gblas.books.backend.dto;

import gblas.books.backend.dto.question.QuestionRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record QuizRequest(
        @NotEmpty(message = "Questions list cannot be empty")
        @Valid List<@Valid QuestionRequest> questions
) { }

