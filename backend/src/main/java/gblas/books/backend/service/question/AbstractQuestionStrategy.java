package gblas.books.backend.service.question;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;

public abstract class AbstractQuestionStrategy<T extends QuestionRequest, Q extends QuestionEntity> implements QuestionStrategy {

    @Override
    public QuestionEntity createQuestion(QuestionRequest request, QuizEntity quiz) {
        if (!getRequestType().isInstance(request)) {
            throw new IllegalArgumentException("Invalid request type for " + getQuestionType() + " question");
        }

        T typedRequest = getRequestType().cast(request);
        Q question = createTypedQuestion(typedRequest);

        question.setExplanation(typedRequest.explanation());
        question.setType(getQuestionType());
        question.setPrompt(typedRequest.prompt());
        question.setQuiz(quiz);

        return question;
    }

    protected abstract Q createTypedQuestion(T request);
    public abstract Class<T> getRequestType();
    public abstract QuestionEntity.QuestionType getQuestionType();
}
