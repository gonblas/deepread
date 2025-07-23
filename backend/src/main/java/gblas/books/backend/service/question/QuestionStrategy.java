package gblas.books.backend.service.question;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuestionEntity.QuestionType;
import gblas.books.backend.entity.QuizEntity;

public interface QuestionStrategy {
    public QuestionEntity createQuestion(QuestionRequest request, QuizEntity quiz);

    public Class<?> getRequestType();

    public QuestionType getQuestionType();
}
