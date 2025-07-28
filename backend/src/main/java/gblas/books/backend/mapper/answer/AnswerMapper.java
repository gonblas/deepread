package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AnswerMapper {

    AnswerMapper INSTANCE =  Mappers.getMapper(AnswerMapper.class);

    default AnswerResponse toDto(AnswerEntity entity, @Context AnswerMapperFactory factory) {
        TypedAnswerMapper<AnswerRequest, AnswerEntity, AnswerResponse> casted = getTypedMapper(entity.getType(), factory);
        return casted.toDto(entity);
    }

    default AnswerEntity toEntity(AnswerRequest request, QuizAttemptEntity quizAttempt, QuestionEntity question, @Context AnswerMapperFactory factory) {
        TypedAnswerMapper<AnswerRequest, AnswerEntity, AnswerResponse> castedMapper = getTypedMapper(request.type(), factory);
        return castedMapper.toEntity(request, quizAttempt, question);
    }

    private TypedAnswerMapper<AnswerRequest, AnswerEntity, AnswerResponse> getTypedMapper(QuestionEntity.QuestionType type, @Context AnswerMapperFactory factory) {
        return (TypedAnswerMapper<AnswerRequest, AnswerEntity, AnswerResponse>) factory.getByType(type);
    }


}
