package gblas.books.backend.dto;

import gblas.books.backend.entity.QuestionEntity.QuestionType;

import java.util.UUID;

public record OpenQuestionResponse(
        UUID id,
        QuestionType question_type,
        String prompt,
        String explanation,
        String expectedAnswer
) implements QuestionResponse {

}
