package gblas.books.backend.dto.answer;

import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;

public interface AnswerResponse {
    QuestionResponse question();
    QuestionType type();
    Boolean isCorrect();
}
