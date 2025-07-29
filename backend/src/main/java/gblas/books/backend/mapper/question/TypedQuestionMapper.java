package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.dto.question.UpdateQuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import org.mapstruct.MappingTarget;

public interface TypedQuestionMapper<
        T extends QuestionRequest,
        E extends QuestionEntity,
        R extends QuestionResponse,
        U extends UpdateQuestionRequest> {

    E toEntity(T request);
    R toDto(E entity);
    void updateEntity(U request, @MappingTarget E entity);

    QuestionEntity.QuestionType getQuestionType();
    Class<T> getRequestClass();
    Class<E> getEntityClass();
}




