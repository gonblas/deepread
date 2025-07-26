package gblas.books.backend.dto;

import gblas.books.backend.entity.question.QuestionEntity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrueOrFalseQuestionRequest(
        @NotNull QuestionType question_type,
        @NotBlank String prompt,
        String explanation,
        @NotNull Boolean isAnswerTrue
) implements QuestionRequest {
}