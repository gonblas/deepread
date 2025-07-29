package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.dto.question.UpdateQuestionRequest;
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
        TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse, UpdateQuestionRequest> casted = getTypedMapper(entity.getType(), factory);
        return casted.toDto(entity);
    }

    default QuestionEntity toEntity(QuestionRequest request, @Context QuestionMapperFactory factory) {
        TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse, UpdateQuestionRequest> castedMapper = getTypedMapper(request.type(), factory);
        return castedMapper.toEntity(request);
    }

    default void updateEntity(UpdateQuestionRequest updateQuestionRequest, QuestionEntity entity, @Context QuestionMapperFactory factory) {
        TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse, UpdateQuestionRequest> castedMapper = getTypedMapper(updateQuestionRequest.type(), factory);
        castedMapper.updateEntity(updateQuestionRequest, entity);
    }

    private TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse, UpdateQuestionRequest> getTypedMapper(QuestionEntity.QuestionType type, @Context QuestionMapperFactory factory) {
        return (TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse, UpdateQuestionRequest>) factory.getByType(type);
    }


}
