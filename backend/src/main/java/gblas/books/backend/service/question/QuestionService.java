package gblas.books.backend.service.question;

import gblas.books.backend.dto.QuestionRequest;
import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.QuestionMapper;
import gblas.books.backend.repository.QuestionRepository;
import gblas.books.backend.repository.QuizRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class QuestionService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionFactory questionFactory;


    public QuestionEntity createQuestion(QuestionRequest request, QuizEntity quiz) {
        QuestionStrategy strategy = questionFactory.getQuestionStrategy(request.question_type());

        if (strategy == null) {
            throw new IllegalArgumentException("Question type wrong: " + request.question_type());
        }
        QuestionEntity newQuestion = strategy.createQuestion(request, quiz);
        questionRepository.save(newQuestion);
        return newQuestion;
    }

    public QuestionResponse addQuestion(@Valid UUID quizId, QuestionRequest questionRequest) {
        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
        QuestionEntity newQuestion = createQuestion(questionRequest, quiz);
        return QuestionMapper.INSTANCE.toDto(newQuestion, questionFactory);
    }

    public void deleteQuestion(@Valid UUID quizId, @Valid UUID questionId) {
        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
        QuestionEntity question = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found"));
        quiz.getQuestions().remove(question);
        quizRepository.save(quiz);
    }

//    public QuestionResponse changeQuestion(@Valid UUID quizId, @Valid UUID questionId, @Valid QuestionRequest questionRequest) {
//        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
//        QuestionEntity question = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found"));
//
//    }
//
//    public QuestionResponse updateQuestion(@Valid UUID quizId, @Valid UUID questionId, UpdateQuestionRequest questionRequest) {
//    }
}
