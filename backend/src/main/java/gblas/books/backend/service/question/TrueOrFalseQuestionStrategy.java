package gblas.books.backend.service.question;

import gblas.books.backend.dto.TrueOrFalseQuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.TrueOrFalseQuestionEntity;
import gblas.books.backend.mapper.TrueOrFalseQuestionMapper;
import org.springframework.stereotype.Component;
import gblas.books.backend.dto.QuestionResponse;

@Component
public class TrueOrFalseQuestionStrategy
        extends AbstractQuestionStrategy<TrueOrFalseQuestionRequest, TrueOrFalseQuestionEntity> {

    private final TrueOrFalseQuestionMapper mapper;

    public TrueOrFalseQuestionStrategy(TrueOrFalseQuestionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected TrueOrFalseQuestionEntity createTypedQuestion(TrueOrFalseQuestionRequest request, QuizEntity quiz) {
        return mapper.toEntity(request, quiz);
    }

    @Override
    protected QuestionResponse mapToDto(TrueOrFalseQuestionEntity entity) {
        return mapper.toDto(entity);
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


