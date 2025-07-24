package gblas.books.backend.mapper;

import gblas.books.backend.dto.OpenQuestionResponse;
import gblas.books.backend.entity.OpenQuestionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OpenQuestionMapper {
    OpenQuestionResponse toDto(OpenQuestionEntity entity);
}

