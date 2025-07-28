package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.AnswerEntity;
import org.mapstruct.*;

@MapperConfig(
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
public interface AnswerConfig {

    @Mapping(target = "type", source = "type")
    AnswerResponse ToDto(AnswerEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quizAttempt", source = "quizAttempt")
    @Mapping(target = "type", source = "request.type")
    AnswerEntity toEntity(AnswerRequest request, QuizAttemptEntity quiz);

}