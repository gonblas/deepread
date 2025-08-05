package gblas.books.backend.dto.answer.Open;

import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;

public record OpenAnswerResponse(
        QuestionResponse question,
        QuestionEntity.QuestionType type,
        Boolean isCorrect,
        String answerText
)
implements AnswerResponse {
}
