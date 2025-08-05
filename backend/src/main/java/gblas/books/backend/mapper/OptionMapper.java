package gblas.books.backend.mapper;

import gblas.books.backend.dto.*;
import gblas.books.backend.entity.OptionEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper
public interface OptionMapper {

    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    @Mapping(target = "id", source = "id")
    OptionResponse toDto(OptionEntity chapter);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    OptionEntity toEntity(OptionRequest request);
}
