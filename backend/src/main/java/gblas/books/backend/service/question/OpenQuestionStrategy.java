package gblas.books.backend.service.question;

import gblas.books.backend.dto.question.OpenQuestion.OpenQuestionRequest;
import gblas.books.backend.entity.question.OpenQuestionEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.mapper.question.OpenQuestionMapper;
import org.springframework.stereotype.Component;

@Component
public class OpenQuestionStrategy extends AbstractQuestionStrategy<OpenQuestionRequest, OpenQuestionEntity, OpenQuestionMapper> {

    public OpenQuestionStrategy(OpenQuestionMapper mapper) {
        super(mapper);
    }

    @Override
    public Class<OpenQuestionRequest> getRequestType() {
        return OpenQuestionRequest.class;
    }

    @Override
    public QuestionEntity.QuestionType getQuestionType() {
        return QuestionEntity.QuestionType.OPEN;
    }

    @Override
    protected Class<OpenQuestionEntity> getEntityType() {
        return OpenQuestionEntity.class;
    }
}



