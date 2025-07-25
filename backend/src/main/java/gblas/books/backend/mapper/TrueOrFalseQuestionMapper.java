package gblas.books.backend.mapper;

import gblas.books.backend.dto.TrueOrFalseQuestionRequest;
import gblas.books.backend.dto.TrueOrFalseQuestionResponse;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.TrueOrFalseQuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = QuestionConfig.class)
public interface TrueOrFalseQuestionMapper extends QuestionMapper2<TrueOrFalseQuestionRequest, TrueOrFalseQuestionEntity> {
    @Mapping(target = "isAnswerTrue", source = "isAnswerTrue")
    TrueOrFalseQuestionResponse toDto(TrueOrFalseQuestionEntity entity);

    @Mapping(target = "isAnswerTrue", source = "request.isAnswerTrue")
    TrueOrFalseQuestionEntity toEntity(TrueOrFalseQuestionRequest request, QuizEntity quiz);
}

