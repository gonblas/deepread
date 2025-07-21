package gblas.books.backend.service;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.dto.TrueOrFalseQuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.TrueOrFalseQuestionEntity;
import org.springframework.stereotype.Component;

@Component
public class TrueOrFalseQuestionStrategy implements QuestionStrategy {

    @Override
    public QuestionEntity createQuestion(QuestionRequest request) {
        if (!(request instanceof TrueOrFalseQuestionRequest trueOrFalseQuestionRequest)) {
            throw new IllegalArgumentException("Invalid request type for TRUE_FALSE question");
        }
        TrueOrFalseQuestionEntity question = new TrueOrFalseQuestionEntity();
        question.setIsAnswerTrue(trueOrFalseQuestionRequest.isAnswerTrue());
        question.setExplanation(trueOrFalseQuestionRequest.explanation());
        question.setType(QuestionEntity.QuestionType.TRUE_FALSE);
        question.setPrompt(trueOrFalseQuestionRequest.prompt());

        return question;
    }
}
