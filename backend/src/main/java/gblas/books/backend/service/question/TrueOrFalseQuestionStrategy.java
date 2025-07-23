package gblas.books.backend.service.question;

import gblas.books.backend.dto.TrueOrFalseQuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.TrueOrFalseQuestionEntity;
import org.springframework.stereotype.Component;

@Component
public class TrueOrFalseQuestionStrategy extends AbstractQuestionStrategy<TrueOrFalseQuestionRequest, TrueOrFalseQuestionEntity> {

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
}

