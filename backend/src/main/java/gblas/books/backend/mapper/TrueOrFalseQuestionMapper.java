package gblas.books.backend.mapper;

import gblas.books.backend.dto.TrueOrFalseQuestionRequest;
import gblas.books.backend.dto.TrueOrFalseQuestionResponse;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.TrueOrFalseQuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrueOrFalseQuestionMapper {
    @Mapping(target = "question_type", source = "type")
    TrueOrFalseQuestionResponse toDto(TrueOrFalseQuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", source = "quiz")
    @Mapping(target = "type", source = "request.question_type")
    TrueOrFalseQuestionEntity toEntity(TrueOrFalseQuestionRequest request, QuizEntity quiz);
}

