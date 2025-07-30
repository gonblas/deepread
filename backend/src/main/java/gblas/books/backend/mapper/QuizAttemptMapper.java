package gblas.books.backend.mapper;

import gblas.books.backend.dto.QuizAttemptResponse;
import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.mapper.answer.AnswerMapper;
import gblas.books.backend.mapper.answer.AnswerMapperFactory;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface QuizAttemptMapper {
    QuizAttemptMapper INSTANCE = Mappers.getMapper(QuizAttemptMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "quiz_id", source = "quizVersion.quiz.id")
    @Mapping(target = "answers", source = "answers", qualifiedByName = "answerMapping")
    @Mapping(target = "startedAt", source = "startedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "submittedAt", source = "submittedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    QuizAttemptResponse toDto(QuizAttemptEntity quizAttempt, @Context AnswerMapperFactory answerFactory, @Context QuestionMapperFactory questionFactory);

    @Named("answerMapping")
    default List<AnswerResponse> answerMapping(List<AnswerEntity> answers, @Context AnswerMapperFactory answerFactory, @Context QuestionMapperFactory questionFactory) {
        return answers
                .stream()
                .map(a -> AnswerMapper.INSTANCE.toDto(a, answerFactory, questionFactory))
                .collect(Collectors.toList());
    }
}

