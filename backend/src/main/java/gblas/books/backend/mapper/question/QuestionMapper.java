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
        var mapper = factory.getByType(entity.getType());

        Class<?> expected = mapper.getEntityClass();

        if (!expected.isInstance(entity)) {
            throw new IllegalArgumentException("Invalid entity type. Expected: " + expected + ", got: " + entity.getClass());
        }

        @SuppressWarnings("unchecked")
        TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse> casted =
                (TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse>) mapper;

        return casted.toDto(entity);
    }

    default QuestionEntity toEntity(QuestionRequest request, QuizEntity quiz, @Context QuestionMapperFactory factory) {
        var mapper = factory.getByType(request.type());

        Class<?> expectedRequest = mapper.getRequestClass();

        if (!expectedRequest.isInstance(request)) {
            throw new IllegalArgumentException("Invalid request type. Expected: " + expectedRequest + ", got: " + request.getClass());
        }

        @SuppressWarnings("unchecked")
        TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse> casted =
                (TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse>) mapper;

        return casted.toEntity(request, quiz);
    }

    default void updateEntity(QuestionRequest request, QuestionEntity entity, @Context QuestionMapperFactory factory) {
        var mapper = factory.getByType(request.type());

        Class<?> expectedRequest = mapper.getRequestClass();
        Class<?> expectedEntity = mapper.getEntityClass();

        if (!expectedRequest.isInstance(request)) {
            throw new IllegalArgumentException("Invalid request type. Expected: " + expectedRequest + ", got: " + request.getClass());
        }

        if (!expectedEntity.isInstance(entity)) {
            throw new IllegalArgumentException("Invalid entity type. Expected: " + expectedEntity + ", got: " + entity.getClass());
        }

        @SuppressWarnings("unchecked")
        TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse> casted =
                (TypedQuestionMapper<QuestionRequest, QuestionEntity, QuestionResponse>) mapper;

        casted.updateEntity(request, entity);
    }

}
