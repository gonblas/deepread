package gblas.books.backend.dto.question.OpenQuestion;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OpenQuestionRequest(
        @NotNull QuestionType question_type,
        @NotBlank String prompt,
        String explanation,
        @NotBlank String expectedAnswer
) implements QuestionRequest {

}
