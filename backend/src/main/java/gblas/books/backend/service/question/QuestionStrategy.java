package gblas.books.backend.service.question;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuestionEntity.QuestionType;
import gblas.books.backend.entity.QuizEntity;

public interface QuestionStrategy {
    QuestionEntity createQuestion(QuestionRequest request, QuizEntity quiz);

    Class<?> getRequestType();

    QuestionEntity.QuestionType getQuestionType();

    QuestionResponse toDto(QuestionEntity entity);
}

