package gblas.books.backend.mapper;

import gblas.books.backend.dto.TrueOrFalseQuestionResponse;
import gblas.books.backend.entity.TrueOrFalseQuestionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrueOrFalseQuestionMapper {
    TrueOrFalseQuestionResponse toDto(TrueOrFalseQuestionEntity entity);
}

