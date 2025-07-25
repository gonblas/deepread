package gblas.books.backend.mapper;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;

public interface QuestionMapper2<T extends QuestionRequest, Q extends QuestionEntity> {
    Q toEntity(T request, QuizEntity quiz);
    QuestionResponse toDto(Q entity);
}
