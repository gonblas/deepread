package gblas.books.backend.dto.question.Open;

import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;

import java.util.UUID;

public record OpenQuestionResponse(
        UUID id,
        QuestionEntity.QuestionType type,
        String prompt,
        String explanation,
        String expectedAnswer
) implements QuestionResponse {

}
