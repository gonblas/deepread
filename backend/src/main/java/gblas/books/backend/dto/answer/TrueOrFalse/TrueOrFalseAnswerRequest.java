package gblas.books.backend.dto.answer.TrueOrFalse;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.entity.question.QuestionEntity;

import java.util.UUID;

public record TrueOrFalseAnswerRequest(
        UUID questionId,
        QuestionEntity.QuestionType type,
        Boolean isCorrect,
        Boolean answer
) implements AnswerRequest {

}
