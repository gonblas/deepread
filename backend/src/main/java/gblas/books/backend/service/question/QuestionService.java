package gblas.books.backend.service.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.question.QuestionMapper;
import gblas.books.backend.repository.QuestionRepository;
import gblas.books.backend.repository.QuizRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
        QuestionStrategy strategy = questionFactory.getQuestionStrategy(request.type());
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

    public QuestionResponse changeQuestion(@Valid UUID quizId, @Valid UUID questionId, @Valid QuestionRequest questionRequest) {
        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
        QuestionEntity questionEntity = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found"));
        questionRepository.delete(questionEntity);
        questionEntity = createQuestion(questionRequest, quiz);
        return QuestionMapper.INSTANCE.toDto(questionEntity, questionFactory);
    }

    public QuestionResponse updateQuestion(@Valid UUID quizId, @Valid UUID questionId, @Valid QuestionRequest questionRequest) throws BadRequestException {
        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
        QuestionEntity questionEntity = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found"));

        if(!questionEntity.getType().equals(questionRequest.type())) {
            throw new BadRequestException("Question type not match");
        }

        QuestionStrategy strategy = questionFactory.getQuestionStrategy(questionRequest.type());
        strategy.updateQuestion(questionRequest, questionEntity);
        questionRepository.save(questionEntity);
        return QuestionMapper.INSTANCE.toDto(questionEntity, questionFactory);
    }

}
