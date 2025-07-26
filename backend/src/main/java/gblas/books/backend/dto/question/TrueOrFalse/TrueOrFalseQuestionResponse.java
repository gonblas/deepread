package gblas.books.backend.dto.question.TrueOrFalse;

import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;

import java.util.UUID;

public record TrueOrFalseQuestionResponse(
        UUID id,
        QuestionEntity.QuestionType question_type,
        String prompt,
        String explanation,
        Boolean isAnswerTrue
) implements QuestionResponse {
}
