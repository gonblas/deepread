package gblas.books.backend.service;

import gblas.books.backend.dto.OpenQuestionRequest;
import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.entity.OpenQuestionEntity;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import org.springframework.stereotype.Component;

@Component
public class OpenQuestionStrategy extends AbstractQuestionStrategy<OpenQuestionRequest, OpenQuestionEntity> {

    @Override
    protected OpenQuestionEntity createTypedQuestion(OpenQuestionRequest request) {
        OpenQuestionEntity question = new OpenQuestionEntity();
        question.setExpectedAnswer(request.expectedAnswer());
        return question;
    }

    @Override
    protected Class<OpenQuestionRequest> requestType() {
        return OpenQuestionRequest.class;
    }

    @Override
    protected QuestionEntity.QuestionType getType() {
        return QuestionEntity.QuestionType.OPEN;
    }
}

