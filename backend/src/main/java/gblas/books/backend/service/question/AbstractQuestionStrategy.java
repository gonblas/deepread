package gblas.books.backend.service.question;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;

public abstract class AbstractQuestionStrategy<T extends QuestionRequest, Q extends QuestionEntity>
        implements QuestionStrategy {

    @Override
    public QuestionEntity createQuestion(QuestionRequest request, QuizEntity quiz) {
        if (!getRequestType().isInstance(request)) {
            throw new IllegalArgumentException("Invalid request type for " + getQuestionType() + " question");
        }

        T typedRequest = getRequestType().cast(request);

        return createTypedQuestion(typedRequest, quiz);
    }

    @Override
    public QuestionResponse toDto(QuestionEntity entity) {
        if (!getEntityType().isInstance(entity)) {
            throw new IllegalArgumentException("Invalid entity type for " + getQuestionType());
        }

        Q typedEntity = getEntityType().cast(entity);
        return mapToDto(typedEntity);
    }

    protected abstract Q createTypedQuestion(T request, QuizEntity quiz);

    public abstract Class<T> getRequestType();

    public abstract QuestionEntity.QuestionType getQuestionType();

    protected abstract Class<Q> getEntityType();

    protected abstract QuestionResponse mapToDto(Q entity);
}

