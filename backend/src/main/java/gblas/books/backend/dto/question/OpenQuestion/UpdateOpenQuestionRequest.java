package gblas.books.backend.dto.question.OpenQuestion;

import gblas.books.backend.dto.question.UpdateQuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.validation.NotBlankIfPresent;

public record UpdateOpenQuestionRequest(
        @NotBlankIfPresent QuestionEntity.QuestionType type,
        @NotBlankIfPresent String prompt,
        String explanation,
        @NotBlankIfPresent String expectedAnswer
) implements UpdateQuestionRequest {

}

