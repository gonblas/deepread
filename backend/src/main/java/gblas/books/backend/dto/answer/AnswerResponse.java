package gblas.books.backend.dto.answer;


import gblas.books.backend.entity.question.QuestionEntity.QuestionType;

import java.util.UUID;

public class AnswerResponse {
    UUID question_id;
    QuestionType type;
    Boolean isCorrect;
}
