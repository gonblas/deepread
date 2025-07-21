package gblas.books.backend.service;

import gblas.books.backend.entity.QuestionEntity.QuestionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class QuestionFactory {
    private final TrueOrFalseQuestionStrategy trueOrFalseStrategy;
    private final OpenQuestionStrategy openStrategy;
    private final Map<QuestionType, QuestionStrategy> strategies;

    public QuestionFactory(TrueOrFalseQuestionStrategy trueOrFalseStrategy, OpenQuestionStrategy openStrategy) {
        this.trueOrFalseStrategy = trueOrFalseStrategy;
        this.openStrategy = openStrategy;
        this.strategies = new EnumMap<>(QuestionType.class);
        this.strategies.put(QuestionType.TRUE_FALSE, trueOrFalseStrategy);
        this.strategies.put(QuestionType.OPEN, openStrategy);
    }


    public QuestionStrategy getQuestionStrategy(QuestionType questionType) {
        QuestionStrategy questionStrategy = strategies.get(questionType);
        if (questionStrategy == null) {
            throw new IllegalArgumentException(String.format("No strategy found for type %s", questionType));
        }
        return questionStrategy;
    }

}
