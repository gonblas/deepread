package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.dto.question.UpdateQuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import org.mapstruct.*;

@MapperConfig(
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
public interface QuestionConfig {

    @Mapping(target = "type", source = "type")
    QuestionResponse ToDto(QuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versions", ignore = true)
    @Mapping(target = "type", source = "request.type")
    QuestionEntity toEntity(QuestionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "versions", ignore = true) // revisar esto, deberia agregarse una nueva versión
    @Mapping(target = "type", source = "request.type")
    void updateEntity(UpdateQuestionRequest request, @MappingTarget QuestionEntity entity);

}