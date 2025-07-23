package gblas.books.backend.service.question;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionFactory questionFactory;
    private final QuestionRepository questionRepository;

    public QuestionEntity createQuestion(QuestionRequest request, QuizEntity quiz) {
        QuestionStrategy strategy = questionFactory.getQuestionStrategy(request.question_type());

        if (strategy == null) {
            throw new IllegalArgumentException("Question type wrong: " + request.question_type());
        }
        QuestionEntity newQuestion = strategy.createQuestion(request, quiz);
        questionRepository.save(newQuestion);
        return newQuestion;
    }

}
