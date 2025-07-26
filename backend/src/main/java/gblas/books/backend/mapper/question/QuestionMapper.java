package gblas.books.backend.mapper;

import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.entity.QuestionEntity;
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
}
