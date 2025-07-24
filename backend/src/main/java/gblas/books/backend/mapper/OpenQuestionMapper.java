package gblas.books.backend.mapper;

import gblas.books.backend.dto.OpenQuestionRequest;
import gblas.books.backend.dto.OpenQuestionResponse;
import gblas.books.backend.entity.OpenQuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OpenQuestionMapper {
    @Mapping(target = "question_type", source = "type")
    OpenQuestionResponse toDto(OpenQuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", source = "quiz")
    @Mapping(target = "type", source = "request.question_type")
    OpenQuestionEntity toEntity(OpenQuestionRequest request, QuizEntity quiz);
}

