package gblas.books.backend.service.question;

import gblas.books.backend.dto.OpenQuestionRequest;
import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.entity.OpenQuestionEntity;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.mapper.OpenQuestionMapper;
import org.springframework.stereotype.Component;

@Component
public class OpenQuestionStrategy extends AbstractQuestionStrategy<OpenQuestionRequest, OpenQuestionEntity> {

    private final OpenQuestionMapper mapper;

    public OpenQuestionStrategy(OpenQuestionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected OpenQuestionEntity createTypedQuestion(OpenQuestionRequest request) {
        OpenQuestionEntity question = new OpenQuestionEntity();
        question.setExpectedAnswer(request.expectedAnswer());
        return question;
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

    @Override
    protected QuestionResponse mapToDto(OpenQuestionEntity entity) {
        return mapper.toDto(entity);
    }
}



