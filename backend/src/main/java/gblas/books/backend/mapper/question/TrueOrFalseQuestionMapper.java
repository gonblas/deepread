package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.TrueOrFalse.TrueOrFalseQuestionRequest;
import gblas.books.backend.dto.question.TrueOrFalse.TrueOrFalseQuestionResponse;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.question.TrueOrFalseQuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = QuestionConfig.class)
public interface TrueOrFalseQuestionMapper extends TypedQuestionMapper<TrueOrFalseQuestionRequest, TrueOrFalseQuestionEntity> {
    @Mapping(target = "isAnswerTrue", source = "isAnswerTrue")
    TrueOrFalseQuestionResponse toDto(TrueOrFalseQuestionEntity entity);

    @Mapping(target = "isAnswerTrue", source = "request.isAnswerTrue")
    TrueOrFalseQuestionEntity toEntity(TrueOrFalseQuestionRequest request, QuizEntity quiz);

    @Mapping(target = "isAnswerTrue", source = "request.isAnswerTrue")
    @Mapping(target = "type", source = "request.type")
    void updateEntity(TrueOrFalseQuestionRequest request, @MappingTarget TrueOrFalseQuestionEntity entity);
}

