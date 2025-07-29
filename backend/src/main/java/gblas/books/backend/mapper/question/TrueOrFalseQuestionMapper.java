package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.TrueOrFalse.TrueOrFalseQuestionRequest;
import gblas.books.backend.dto.question.TrueOrFalse.TrueOrFalseQuestionResponse;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.question.TrueOrFalseQuestionEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", config = QuestionConfig.class)
public interface TrueOrFalseQuestionMapper extends TypedQuestionMapper<TrueOrFalseQuestionRequest, TrueOrFalseQuestionEntity, TrueOrFalseQuestionResponse> {
    @Mapping(target = "isAnswerTrue", source = "isAnswerTrue")
    TrueOrFalseQuestionResponse toDto(TrueOrFalseQuestionEntity entity);

    @Mapping(target = "isAnswerTrue", source = "request.isAnswerTrue")
    TrueOrFalseQuestionEntity toEntity(TrueOrFalseQuestionRequest request, QuizEntity quiz);

    default QuestionEntity.QuestionType getQuestionType() {
        return QuestionEntity.QuestionType.TRUE_FALSE;
    }

    @Override
    default Class<TrueOrFalseQuestionRequest> getRequestClass() {
        return TrueOrFalseQuestionRequest.class;
    }

    @Override
    default Class<TrueOrFalseQuestionEntity> getEntityClass() {
        return TrueOrFalseQuestionEntity.class;
    }
}

