package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.OpenQuestion.OpenQuestionRequest;
import gblas.books.backend.dto.question.OpenQuestion.OpenQuestionResponse;
import gblas.books.backend.entity.question.OpenQuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = QuestionConfig.class)
public interface OpenQuestionMapper extends QuestionMapper2<OpenQuestionRequest, OpenQuestionEntity> {
    @Mapping(target = "expectedAnswer", source = "expectedAnswer")
    OpenQuestionResponse toDto(OpenQuestionEntity entity);

    @Mapping(target = "expectedAnswer", source = "request.expectedAnswer")
    OpenQuestionEntity toEntity(OpenQuestionRequest request, QuizEntity quiz);

    @Mapping(target = "expectedAnswer", source = "request.expectedAnswer")
    @Mapping(target = "type", source = "request.type")
    void updateEntity(OpenQuestionRequest request, @MappingTarget OpenQuestionEntity entity);
}

