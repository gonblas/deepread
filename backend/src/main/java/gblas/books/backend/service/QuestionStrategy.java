package gblas.books.backend.service;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.entity.QuestionEntity;

public interface QuestionStrategy {
    QuestionEntity createQuestion(QuestionRequest request);

    public interface QuestionCreationStrategy {
        QuestionEntity createQuestion(QuestionRequest request);
    }
}
