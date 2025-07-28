package gblas.books.backend.dto.answer.OpenAnswer;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;

import java.util.UUID;

public record OpenAnswerRequest(
        UUID questionId,
        QuestionType type,
        String answerText
) implements AnswerRequest {
}
