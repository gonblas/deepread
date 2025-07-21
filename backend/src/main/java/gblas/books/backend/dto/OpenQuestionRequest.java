package gblas.books.backend.dto;

import gblas.books.backend.entity.QuestionEntity;

import java.util.UUID;

public record OpenQuestionRequest(
        QuestionEntity.QuestionType question_type,
        String prompt,
        String explanation,
        String expectedAnswer
) implements QuestionRequest {

}
