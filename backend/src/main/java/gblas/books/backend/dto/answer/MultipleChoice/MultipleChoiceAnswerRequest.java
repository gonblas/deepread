package gblas.books.backend.dto.answer.MultipleChoice;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record MultipleChoiceAnswerRequest(
        @NotNull QuestionType type,
        @NotNull UUID questionId,
        @NotEmpty List<UUID> optionIds
        ) implements AnswerRequest {
}
