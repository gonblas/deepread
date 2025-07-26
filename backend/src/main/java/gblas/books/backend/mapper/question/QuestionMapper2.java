package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import org.mapstruct.MappingTarget;

public interface QuestionMapper2<T extends QuestionRequest, Q extends QuestionEntity> {
    Q toEntity(T request, QuizEntity quiz);
    QuestionResponse toDto(Q entity);
    void updateEntity(T request, @MappingTarget Q entity);
}
