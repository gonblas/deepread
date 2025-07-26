package gblas.books.backend.dto.question.TrueOrFalse;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrueOrFalseQuestionRequest(
        @NotNull QuestionType type,
        @NotBlank String prompt,
        String explanation,
        @NotNull Boolean isAnswerTrue
) implements QuestionRequest {
}