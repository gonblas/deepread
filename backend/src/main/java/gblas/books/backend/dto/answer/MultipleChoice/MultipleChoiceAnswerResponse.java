package gblas.books.backend.dto.answer.MultipleChoice;

import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;

import java.util.List;
import java.util.UUID;

public record MultipleChoiceAnswerResponse(
        QuestionResponse question,
        QuestionEntity.QuestionType type,
        Boolean isCorrect,
        List<UUID> optionsSelected
)
implements AnswerResponse {
}
