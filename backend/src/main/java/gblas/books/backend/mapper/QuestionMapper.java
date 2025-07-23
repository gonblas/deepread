package gblas.books.backend.mapper;

import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.service.question.QuestionFactory;
import gblas.books.backend.service.question.QuestionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final QuestionFactory questionFactory;

    public QuestionResponse dtoFrom(QuestionEntity entity) {
        QuestionStrategy strategy = questionFactory.getQuestionStrategy(entity.getType());
        return strategy.toDto(entity);
    }
}
