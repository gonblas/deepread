package gblas.books.backend.service;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;

public abstract class AbstractQuestionStrategy<T extends QuestionRequest, Q extends QuestionEntity> implements QuestionStrategy {

    @Override
    public QuestionEntity createQuestion(QuestionRequest request) {
        if (!requestType().isInstance(request)) {
            throw new IllegalArgumentException("Invalid request type for " + getType() + " question");
        }

        T typedRequest = requestType().cast(request);
        Q question = createTypedQuestion(typedRequest);

        question.setExplanation(typedRequest.explanation());
        question.setType(getType());
        question.setPrompt(typedRequest.prompt());

        return question;
    }

    protected abstract Q createTypedQuestion(T request);
    protected abstract Class<T> requestType();
    protected abstract QuestionEntity.QuestionType getType();
}
