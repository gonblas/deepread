package gblas.books.backend.dto.answer.TrueOrFalse;

import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;

import java.util.UUID;

public record TrueOrFalseAnswerResponse(
        QuestionResponse question,
        QuestionEntity.QuestionType type,
        Boolean isCorrect,
        Boolean answer
) implements AnswerResponse {
}
