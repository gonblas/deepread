package gblas.books.backend.mapper;

import gblas.books.backend.dto.OpenQuestionRequest;
import gblas.books.backend.dto.OpenQuestionResponse;
import gblas.books.backend.entity.OpenQuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = QuestionConfig.class)
public interface OpenQuestionMapper {
    @Mapping(target = "expectedAnswer", source = "expectedAnswer")
    OpenQuestionResponse toDto(OpenQuestionEntity entity);

    @Mapping(target = "expectedAnswer", source = "request.expectedAnswer")
    OpenQuestionEntity toEntity(OpenQuestionRequest request, QuizEntity quiz);
}

