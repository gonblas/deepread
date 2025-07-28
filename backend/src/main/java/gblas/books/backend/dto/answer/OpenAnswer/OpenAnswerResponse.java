package gblas.books.backend.dto.answer.OpenAnswer;

import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.entity.question.QuestionEntity;

import java.util.UUID;

public record OpenAnswerResponse(
        UUID question_id,
        QuestionEntity.QuestionType type,
        Boolean isCorrect,
        String answerText
)
implements AnswerResponse {
}
