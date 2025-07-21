package gblas.books.backend.service;

import gblas.books.backend.dto.OpenQuestionRequest;
import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.entity.OpenQuestionEntity;
import gblas.books.backend.entity.QuestionEntity;
import org.springframework.stereotype.Component;

@Component
public class OpenQuestionStrategy implements QuestionStrategy {

    @Override
    public QuestionEntity createQuestion(QuestionRequest request) {
        if (!(request instanceof OpenQuestionRequest openQuestionRequest)) {
            throw new IllegalArgumentException("Invalid request type for TRUE_FALSE question");
        }
        OpenQuestionEntity question = new OpenQuestionEntity();
        question.setExpectedAnswer(openQuestionRequest.expectedAnswer());
        question.setExplanation(openQuestionRequest.explanation());
        question.setType(QuestionEntity.QuestionType.OPEN);
        question.setPrompt(openQuestionRequest.prompt());

        return question;
    }
}
