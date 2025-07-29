package gblas.books.backend.service;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.dto.question.UpdateQuestionRequest;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.QuizVersionEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.question.QuestionMapper;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
import gblas.books.backend.repository.QuestionRepository;
import gblas.books.backend.repository.QuizAttemptRepository;
import gblas.books.backend.repository.QuizRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class QuestionService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    //private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionMapperFactory questionMapperFactory;
    //private final QuizVersionService quizVersionService;


    public QuestionEntity createQuestion(QuestionRequest request, QuizVersionEntity quizVersion) {
        QuestionEntity newQuestion = QuestionMapper.INSTANCE.toEntity(request, questionMapperFactory);

        quizVersion.getQuestions().add(newQuestion);
        questionRepository.save(newQuestion);
        newQuestion.getVersions().add(quizVersion);

        return newQuestion;
    }

//
//    public QuestionResponse addQuestion(@Valid UUID quizId, QuestionRequest questionRequest) {
//        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
//        QuestionEntity newQuestion = createQuestion(questionRequest, quiz);
//
//        return QuestionMapper.INSTANCE.toDto(newQuestion, questionMapperFactory);
//    }
//
//    public void deleteQuestion(@Valid UUID quizId, @Valid UUID questionId) {
//        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
//        QuestionEntity question = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found"));
//        QuizVersionEntity quizLastVersion = quizVersionService.getLastQuizVersionEntity(quiz);
//        quizLastVersion.getQuestions().remove(question);
//        quizRepository.save(quiz);
//    }
//
//    public QuestionResponse changeQuestion(@Valid UUID quizId, @Valid UUID questionId, @Valid QuestionRequest questionRequest) {
//        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
//        QuestionEntity questionEntity = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found"));
//        questionRepository.delete(questionEntity);
//        questionEntity = createQuestion(questionRequest, quiz);
//        return QuestionMapper.INSTANCE.toDto(questionEntity, questionMapperFactory);
//    }
//
//    public QuestionResponse updateQuestion(@Valid UUID quizId, @Valid UUID questionId, @Valid UpdateQuestionRequest updateQuestionRequest) throws BadRequestException {
//        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
//        QuestionEntity questionEntity = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found"));
//
//        if(!questionEntity.getType().equals(updateQuestionRequest.type())) {
//            throw new BadRequestException("Question type not match");
//        }
//
//        QuestionMapper.INSTANCE.updateEntity(updateQuestionRequest, questionEntity, questionMapperFactory);
//        questionRepository.save(questionEntity);
//        return QuestionMapper.INSTANCE.toDto(questionEntity, questionMapperFactory);
//    }

}
