package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;

public interface TypedQuestionMapper<
        T extends QuestionRequest,
        E extends QuestionEntity,
        R extends QuestionResponse> {

    E toEntity(T request);
    R toDto(E entity);

    QuestionEntity.QuestionType getQuestionType();
    Class<T> getRequestClass();
    Class<E> getEntityClass();
}




