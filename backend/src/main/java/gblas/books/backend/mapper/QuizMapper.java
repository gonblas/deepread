package gblas.books.backend.mapper;

import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.mapper.question.QuestionMapper;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface QuizMapper {
    QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "chapter", source = "chapter")
    @Mapping(target = "questions", source = "questions", qualifiedByName = "questionMapping")
    QuizResponse toDto(QuizEntity quiz, @Context QuestionMapperFactory factory);

    @Named("questionMapping")
    default List<QuestionResponse> questionMapping(List<QuestionEntity> questions, @Context QuestionMapperFactory factory) {
        return questions
                .stream()
                .map(q -> QuestionMapper.INSTANCE.toDto(q, factory))
                .collect(Collectors.toList());
    }
}
