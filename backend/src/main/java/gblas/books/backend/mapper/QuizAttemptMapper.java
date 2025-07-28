package gblas.books.backend.mapper;

import gblas.books.backend.dto.QuizAttemptResponse;
import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.mapper.answer.AnswerMapper;
import gblas.books.backend.mapper.answer.AnswerMapperFactory;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface QuizAttemptMapper {
    QuizAttemptMapper INSTANCE = Mappers.getMapper(QuizAttemptMapper.class);

    @Mapping(target = "quiz_id", source = "id")
    @Mapping(target = "answers", source = "answers", qualifiedByName = "answerMapping")
    QuizAttemptResponse toDto(QuizAttemptEntity quizAttempt, @Context AnswerMapperFactory factory);

    @Named("answerMapping")
    default List<AnswerResponse> answerMapping(List<AnswerEntity> answers, @Context AnswerMapperFactory factory) {
        return answers
                .stream()
                .map(a -> AnswerMapper.INSTANCE.toDto(a, factory))
                .collect(Collectors.toList());
    }

}

