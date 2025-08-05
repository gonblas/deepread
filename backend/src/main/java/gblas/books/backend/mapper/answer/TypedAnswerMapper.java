package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.mapper.question.QuestionMapper;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
import org.hibernate.Hibernate;
import org.mapstruct.Context;
import org.mapstruct.Named;

public interface TypedAnswerMapper<
        T extends AnswerRequest,
        E extends AnswerEntity,
        R extends AnswerResponse> {

    E toEntity(T request, QuizAttemptEntity quiz, QuestionEntity question);
    R toDto(E entity, @Context QuestionMapperFactory factory);

    @Named("questionMapping")
    default QuestionResponse questionMapping(AnswerEntity entity, @Context QuestionMapperFactory factory) {
        // Unproxies the Hibernate object to get the actual question instance,
        // required for proper mapping with MapStruct.
        QuestionEntity real = (QuestionEntity) Hibernate.unproxy(entity.getQuestion());
        return QuestionMapper.INSTANCE.toDto(real, factory);
    }

    QuestionEntity.QuestionType getAnswerType();
    Class<T> getRequestClass();
    Class<E> getEntityClass();
}




