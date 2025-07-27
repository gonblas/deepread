package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface QuestionMapper {

    QuestionMapper INSTANCE =  Mappers.getMapper(QuestionMapper.class);

    default QuestionResponse toDto(QuestionEntity entity, @Context QuestionMapperFactory factory) {
        TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse> casted = getTypedMapper(entity.getType(), factory);
        return casted.toDto(entity);
    }

    default QuestionEntity toEntity(QuestionRequest request, QuizEntity quiz, @Context QuestionMapperFactory factory) {
        TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse> castedMapper = getTypedMapper(request.type(), factory);
        return castedMapper.toEntity(request, quiz);
    }

    default void updateEntity(QuestionRequest request, QuestionEntity entity, @Context QuestionMapperFactory factory) {
        TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse> castedMapper = getTypedMapper(request.type(), factory);
        castedMapper.updateEntity(request, entity);
    }

    private TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse> getTypedMapper(QuestionEntity.QuestionType type, @Context QuestionMapperFactory factory) {
        return (TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse>) factory.getByType(type);
    }


}
