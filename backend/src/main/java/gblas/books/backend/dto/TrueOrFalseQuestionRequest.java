package gblas.books.backend.dto;

import gblas.books.backend.entity.QuestionEntity;

import java.util.UUID;

public record TrueOrFalseQuestionRequest(
        QuestionEntity.QuestionType question_type,
        String prompt,
        String explanation,
        boolean isAnswerTrue
) implements QuestionRequest {
}