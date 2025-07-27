package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.service.question.QuestionFactory;
import gblas.books.backend.service.question.QuestionStrategy;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface QuestionMapper {

    QuestionMapper INSTANCE =  Mappers.getMapper(QuestionMapper.class);

    default QuestionResponse toDto(QuestionEntity entity, @Context QuestionFactory factory) {
        QuestionStrategy strategy = factory.getQuestionStrategy(entity.getType());
        return strategy.toDto(entity);
    }

    default QuestionEntity toEntity(QuestionRequest request, QuizEntity quiz, @Context QuestionFactory factory) {
        QuestionStrategy strategy = factory.getQuestionStrategy(request.type());
        return strategy.createQuestion(request, quiz);
    }
}
