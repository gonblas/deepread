package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.TrueOrFalse.TrueOrFalseAnswerRequest;
import gblas.books.backend.dto.answer.TrueOrFalse.TrueOrFalseAnswerResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.TrueOrFalseAnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
import org.mapstruct.*;

@Mapper(componentModel = "spring", config = AnswerConfig.class)
public interface TrueOrFalseAnswerMapper extends TypedAnswerMapper<TrueOrFalseAnswerRequest, TrueOrFalseAnswerEntity, TrueOrFalseAnswerResponse> {
    @Mapping(target = "answer", source = "answer")
    TrueOrFalseAnswerResponse toDto(TrueOrFalseAnswerEntity entity, @Context QuestionMapperFactory factory);

    @Mapping(target = "answer", source = "request.answer")
    TrueOrFalseAnswerEntity toEntity(TrueOrFalseAnswerRequest request, QuizAttemptEntity quizAttempt, QuestionEntity question);

    default QuestionEntity.QuestionType getAnswerType() {
        return QuestionEntity.QuestionType.TRUE_FALSE;
    }

    @Override
    default Class<TrueOrFalseAnswerRequest> getRequestClass() {
        return TrueOrFalseAnswerRequest.class;
    }

    @Override
    default Class<TrueOrFalseAnswerEntity> getEntityClass() {
        return TrueOrFalseAnswerEntity.class;
    }
}

