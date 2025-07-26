package gblas.books.backend.service.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.mapper.question.QuestionMapper2;

public abstract class AbstractQuestionStrategy<T extends QuestionRequest, Q extends QuestionEntity, M extends QuestionMapper2<T, Q>>
        implements QuestionStrategy {

    protected final M mapper;

    protected AbstractQuestionStrategy(M mapper) {
        this.mapper = mapper;
    }

    @Override
    public QuestionEntity createQuestion(QuestionRequest request, QuizEntity quiz) {
        if (!getRequestType().isInstance(request)) {
            throw new IllegalArgumentException("Invalid request type for " + getQuestionType());
        }
        T typedRequest = getRequestType().cast(request);
        return mapper.toEntity(typedRequest, quiz);
    }

    @Override
    public void updateQuestion(QuestionRequest request, QuestionEntity entity) {
        if (!getRequestType().isInstance(request)) {
            throw new IllegalArgumentException("Invalid request type for " + getQuestionType());
        }
        T typedRequest = getRequestType().cast(request);
        Q typedQuestion = getEntityType().cast(entity);
        mapper.updateEntity(typedRequest, typedQuestion);
    }

    @Override
    public QuestionResponse toDto(QuestionEntity entity) {
        if (!getEntityType().isInstance(entity)) {
            throw new IllegalArgumentException("Invalid entity type for " + getQuestionType());
        }
        Q typedEntity = getEntityType().cast(entity);
        return mapper.toDto(typedEntity);
    }

    public abstract Class<T> getRequestType();

    public abstract QuestionEntity.QuestionType getQuestionType();

    protected abstract Class<Q> getEntityType();
}

