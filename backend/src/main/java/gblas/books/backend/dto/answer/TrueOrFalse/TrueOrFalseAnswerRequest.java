package gblas.books.backend.dto.answer.TrueOrFalse;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TrueOrFalseAnswerRequest(
        @NotNull QuestionType type,
        @NotNull UUID questionId,
        @NotNull Boolean answer
) implements AnswerRequest {

}
