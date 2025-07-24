package gblas.books.backend.dto;

import gblas.books.backend.entity.QuestionEntity;

import java.util.UUID;

public record TrueOrFalseQuestionResponse(
        UUID id,
        QuestionEntity.QuestionType question_type,
        String prompt,
        String explanation,
        Boolean isAnswerTrue
) implements QuestionResponse {
}
