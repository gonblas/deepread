package gblas.books.backend.mapper;

import gblas.books.backend.dto.OptionRequest;
import gblas.books.backend.dto.OptionResponse;
import gblas.books.backend.entity.OptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
