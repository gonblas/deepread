package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import org.mapstruct.MappingTarget;

public interface TypedAnswerMapper<
        T extends AnswerRequest,
        E extends AnswerEntity,
        R extends AnswerResponse> {

    E toEntity(T request, QuizAttemptEntity quiz, QuestionEntity question);
    R toDto(E entity);

    QuestionEntity.QuestionType getAnswerType();
    Class<T> getRequestClass();
    Class<E> getEntityClass();
}




