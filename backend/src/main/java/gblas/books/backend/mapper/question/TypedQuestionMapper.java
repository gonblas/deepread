package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import org.mapstruct.MappingTarget;

public interface TypedQuestionMapper<
        T extends QuestionRequest,
        E extends QuestionEntity,
        R extends QuestionResponse> {

    E toEntity(T request, QuizEntity quiz);
    R toDto(E entity);
    void updateEntity(T request, @MappingTarget E entity);

    QuestionEntity.QuestionType getQuestionType();

    Class<T> getRequestClass();
    Class<E> getEntityClass();
    Class<R> getResponseClass();
}




