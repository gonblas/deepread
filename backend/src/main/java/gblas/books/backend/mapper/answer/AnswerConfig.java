package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
import org.mapstruct.Context;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

@MapperConfig(
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
public interface AnswerConfig {

    @Mapping(target = "type", source = "type")
    @Mapping(target = "question", source = "entity", qualifiedByName = "questionMapping")
    AnswerResponse ToDto(AnswerEntity entity, @Context QuestionMapperFactory factory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quizAttempt", source = "quizAttempt")
    @Mapping(target = "type", source = "request.type")
    @Mapping(target = "question", source = "question")
    @Mapping(target = "isCorrect", expression = "java(question.validate(request))")
    AnswerEntity toEntity(AnswerRequest request, QuizAttemptEntity quizAttempt, QuestionEntity question);

}