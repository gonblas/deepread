package gblas.books.backend.dto.answer.TrueOrFalse;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;

import java.util.UUID;

public record TrueOrFalseAnswerRequest(
        QuestionType type,
        UUID questionId,
        Boolean answer
) implements AnswerRequest {

}
