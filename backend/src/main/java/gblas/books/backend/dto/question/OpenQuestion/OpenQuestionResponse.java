package gblas.books.backend.dto.question.OpenQuestion;

import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;

import java.util.UUID;

public record OpenQuestionResponse(
        UUID id,
        QuestionEntity.QuestionType question_type,
        String prompt,
        String explanation,
        String expectedAnswer
) implements QuestionResponse {

}
