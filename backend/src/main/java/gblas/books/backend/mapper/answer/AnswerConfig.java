package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import org.mapstruct.*;

@MapperConfig(
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
public interface AnswerConfig {

    @Mapping(target = "type", source = "type")
    @Mapping(target = "question_id", source = "question.id")
    AnswerResponse ToDto(AnswerEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quizAttempt", source = "quizAttempt")
    @Mapping(target = "type", source = "request.type")
    @Mapping(target = "isCorrect", ignore = true) //cambiar esto
    AnswerEntity toEntity(AnswerRequest request, QuizAttemptEntity quizAttempt, QuestionEntity question);

}