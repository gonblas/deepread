package gblas.books.backend.service;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionFactory questionFactory;


    public QuestionEntity createQuestion(QuestionRequest request) {
        QuestionStrategy strategy = questionFactory.getQuestionStrategy(request.question_type());

        if (strategy == null) {
            throw new IllegalArgumentException("Question type wrong: " + request.question_type());
        }

        return strategy.createQuestion(request);
    }

}
