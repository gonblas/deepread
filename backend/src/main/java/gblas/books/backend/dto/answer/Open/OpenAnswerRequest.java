package gblas.books.backend.dto.answer.Open;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OpenAnswerRequest(
        @NotNull QuestionType type,
        @NotNull UUID questionId,
        @NotEmpty String answerText
) implements AnswerRequest {
}
