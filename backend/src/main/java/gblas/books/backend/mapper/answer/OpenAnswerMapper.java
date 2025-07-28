package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.OpenAnswer.OpenAnswerRequest;
import gblas.books.backend.dto.answer.OpenAnswer.OpenAnswerResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.OpenAnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = AnswerConfig.class)
public interface OpenAnswerMapper extends TypedAnswerMapper<OpenAnswerRequest, OpenAnswerEntity, OpenAnswerResponse> {
    @Mapping(target = "answerText", source = "answerText")
    OpenAnswerResponse toDto(OpenAnswerEntity entity);

    @Mapping(target = "answerText", source = "request.answerText")
    @Mapping(target = "quizAttempt", source = "quizAttempt")
    OpenAnswerEntity toEntity(OpenAnswerRequest request, QuizAttemptEntity quizAttempt, QuestionEntity question);

    default QuestionEntity.QuestionType getAnswerType() {
        return QuestionEntity.QuestionType.OPEN;
    }

    @Override
    default Class<OpenAnswerRequest> getRequestClass() {
        return OpenAnswerRequest.class;
    }

    @Override
    default Class<OpenAnswerEntity> getEntityClass() {
        return OpenAnswerEntity.class;
    }
}

