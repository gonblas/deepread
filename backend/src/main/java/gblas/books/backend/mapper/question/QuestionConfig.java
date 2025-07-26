package gblas.books.backend.mapper;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import org.mapstruct.*;

@MapperConfig(
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
public interface QuestionConfig {

    @Mapping(target = "question_type", source = "type")
    QuestionResponse ToDto(QuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", source = "quiz")
    @Mapping(target = "type", source = "request.question_type")
    QuestionEntity toEntity(QuestionRequest request, QuizEntity quiz);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "type", source = "request.question_type")
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(QuestionRequest request, @MappingTarget QuestionEntity entity);

}