package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

@MapperConfig(
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
public interface QuestionConfig {

    @Mapping(target = "type", source = "type")
    QuestionResponse ToDto(QuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versions", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "type", source = "request.type")
    QuestionEntity toEntity(QuestionRequest request);

}