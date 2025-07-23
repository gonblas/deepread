package gblas.books.backend.service.question;

import gblas.books.backend.entity.QuestionEntity.QuestionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class QuestionFactory {

    private final Map<QuestionType, QuestionStrategy> strategies = new EnumMap<>(QuestionType.class);

    public QuestionFactory(List<QuestionStrategy> strategyList) {
        for (QuestionStrategy strategy : strategyList) {
            QuestionType type = strategy.getQuestionType();
            if (strategies.containsKey(type)) {
                throw new IllegalStateException("Duplicate strategy for type: " + type);
            }
            strategies.put(type, strategy);
            log.info("Registered strategy for question type: {}", type);
        }
    }

    public QuestionStrategy getQuestionStrategy(QuestionType questionType) {
        QuestionStrategy strategy = strategies.get(questionType);
        if (strategy == null) {
            throw new IllegalArgumentException(String.format("No strategy found for type %s", questionType));
        }
        return strategy;
    }
}

