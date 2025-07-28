package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.TrueOrFalse.TrueOrFalseAnswerRequest;
import gblas.books.backend.dto.answer.TrueOrFalse.TrueOrFalseAnswerResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.TrueOrFalseAnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = AnswerConfig.class)
public interface TrueOrFalseAnswerMapper extends TypedAnswerMapper<TrueOrFalseAnswerRequest, TrueOrFalseAnswerEntity, TrueOrFalseAnswerResponse> {
    @Mapping(target = "answer", source = "answer")
    TrueOrFalseAnswerResponse toDto(TrueOrFalseAnswerEntity entity);

    @Mapping(target = "answer", source = "request.answer")
    @Mapping(target = "quizAttempt", source = "quizAttempt")
    TrueOrFalseAnswerEntity toEntity(TrueOrFalseAnswerRequest request, QuizAttemptEntity quizAttempt);

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

