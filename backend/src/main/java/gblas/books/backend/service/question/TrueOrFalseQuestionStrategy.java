package gblas.books.backend.service.question;

import gblas.books.backend.dto.question.TrueOrFalse.TrueOrFalseQuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.question.TrueOrFalseQuestionEntity;
import gblas.books.backend.mapper.question.TrueOrFalseQuestionMapper;
import org.springframework.stereotype.Component;

@Component
public class TrueOrFalseQuestionStrategy
        extends AbstractQuestionStrategy<TrueOrFalseQuestionRequest, TrueOrFalseQuestionEntity, TrueOrFalseQuestionMapper> {

    public TrueOrFalseQuestionStrategy(TrueOrFalseQuestionMapper mapper) {
        super(mapper);
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

}


