package gblas.books.backend.dto.answer.OpenAnswer;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.entity.question.QuestionEntity;

import java.util.UUID;

public record OpenAnswerRequest(
        UUID questionId,
        QuestionEntity.QuestionType type,
        Boolean isCorrect,
        String answerText
) implements AnswerRequest {
}
