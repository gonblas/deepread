package gblas.books.backend.service.question;

import gblas.books.backend.dto.TrueFalseQuestionResponse;
import gblas.books.backend.dto.TrueOrFalseQuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.TrueOrFalseQuestionEntity;
import org.springframework.stereotype.Component;
import gblas.books.backend.dto.QuestionResponse;

@Component
public class TrueOrFalseQuestionStrategy
        extends AbstractQuestionStrategy<TrueOrFalseQuestionRequest, TrueOrFalseQuestionEntity> {

    @Override
    protected TrueOrFalseQuestionEntity createTypedQuestion(TrueOrFalseQuestionRequest request) {
        TrueOrFalseQuestionEntity question = new TrueOrFalseQuestionEntity();
        question.setIsAnswerTrue(request.isAnswerTrue());
        return question;
    }

    @Override
    public Class<TrueOrFalseQuestionRequest> getRequestType() {
        return TrueOrFalseQuestionRequest.class;
    }

    @Override
    public QuestionEntity.QuestionType getQuestionType() {
        return QuestionEntity.QuestionType.TRUE_FALSE;
    }

    @Override
    protected Class<TrueOrFalseQuestionEntity> getEntityType() {
        return TrueOrFalseQuestionEntity.class;
    }

    @Override
    protected QuestionResponse mapToDto(TrueOrFalseQuestionEntity entity) {
        return new TrueFalseQuestionResponse(
                entity.getId(),
                entity.getType(),
                entity.getPrompt(),
                entity.getExplanation(),
                entity.getIsAnswerTrue()
        );
    }
}


