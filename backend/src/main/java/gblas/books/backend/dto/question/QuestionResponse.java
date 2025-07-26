package gblas.books.backend.dto.question;

import gblas.books.backend.entity.question.QuestionEntity.QuestionType;

import java.util.UUID;

public interface QuestionResponse {
    UUID id();
    QuestionType question_type();
    String prompt();
    String explanation();
}
