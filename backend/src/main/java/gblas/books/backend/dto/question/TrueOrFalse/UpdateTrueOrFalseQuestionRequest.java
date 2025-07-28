package gblas.books.backend.dto.question.TrueOrFalse;

import gblas.books.backend.dto.question.UpdateQuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.validation.NotBlankIfPresent;
import jakarta.validation.constraints.NotNull;

public record UpdateTrueOrFalseQuestionRequest(
        @NotBlankIfPresent QuestionEntity.QuestionType type,
        @NotBlankIfPresent String prompt,
        String explanation,
        Boolean isAnswerTrue
) implements UpdateQuestionRequest {

}
