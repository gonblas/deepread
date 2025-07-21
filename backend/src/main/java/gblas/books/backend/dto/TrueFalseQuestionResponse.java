package gblas.books.backend.dto;

import gblas.books.backend.entity.QuestionEntity.QuestionType;

import java.util.UUID;

public record TrueFalseQuestionResponse(
        UUID id,
        QuestionType question_type,
        String prompt,
        String explanation,
        boolean isAnswerTrue
) implements QuestionResponse {

}
