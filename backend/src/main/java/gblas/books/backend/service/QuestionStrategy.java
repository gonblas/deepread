package gblas.books.backend.service;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;

public interface QuestionStrategy {
    QuestionEntity createQuestion(QuestionRequest request, QuizEntity quiz);

}
