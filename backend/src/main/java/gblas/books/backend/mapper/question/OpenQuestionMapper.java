package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.OpenQuestion.OpenQuestionRequest;
import gblas.books.backend.dto.question.OpenQuestion.OpenQuestionResponse;
import gblas.books.backend.entity.question.OpenQuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", config = QuestionConfig.class)
public interface OpenQuestionMapper extends TypedQuestionMapper<OpenQuestionRequest, OpenQuestionEntity, OpenQuestionResponse> {
    @Mapping(target = "expectedAnswer", source = "expectedAnswer")
    OpenQuestionResponse toDto(OpenQuestionEntity entity);

    @Mapping(target = "expectedAnswer", source = "request.expectedAnswer")
    OpenQuestionEntity toEntity(OpenQuestionRequest request, QuizEntity quiz);

    default QuestionEntity.QuestionType getQuestionType() {
        return QuestionEntity.QuestionType.OPEN;
    }

    @Override
    default Class<OpenQuestionRequest> getRequestClass() {
        return OpenQuestionRequest.class;
    }

    @Override
    default Class<OpenQuestionEntity> getEntityClass() {
        return OpenQuestionEntity.class;
    }
}

