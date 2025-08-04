package gblas.books.backend.service;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.QuizVersionEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.question.QuestionMapper;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
import gblas.books.backend.repository.QuestionRepository;
import gblas.books.backend.repository.QuizRepository;
import gblas.books.backend.repository.QuizVersionRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class QuestionService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionMapperFactory questionMapperFactory;
    private final QuizVersionService quizVersionService;
    private final QuizVersionRepository quizVersionRepository;

    public QuestionEntity createQuestion(QuestionRequest request, QuizVersionEntity quizVersion) {
        QuestionEntity newQuestion = QuestionMapper.INSTANCE.toEntity(request, questionMapperFactory);
        newQuestion.setQuiz(quizVersion.getQuiz());
        questionRepository.save(newQuestion);
        quizVersion.getQuestions().add(newQuestion);
        quizVersionRepository.save(quizVersion);
        return newQuestion;
    }


    public QuestionResponse addQuestion(@Valid UUID quizId, QuestionRequest questionRequest) {
        log.info("Adding question to quiz with ID: {}", quizId);

        QuizEntity quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));

        QuizVersionEntity newQuizVersion = quizVersionService.updateVersion(quiz);
        log.info("Using new quiz version with ID: {}", newQuizVersion.getId());

        QuestionEntity newQuestion = createQuestion(questionRequest, newQuizVersion);

        // Confirmar estado al final
        QuizVersionEntity versionFromDb = quizVersionRepository.findById(newQuizVersion.getId())
                .orElseThrow(() -> new IllegalStateException("Quiz version not found after update"));

        log.info("Questions in DB version (post-save): {}", versionFromDb.getQuestions().stream().map(QuestionEntity::getId).toList());

        return QuestionMapper.INSTANCE.toDto(newQuestion, questionMapperFactory);
    }

    public void deleteQuestion(@Valid UUID quizId, @Valid UUID questionId) {
        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
        QuestionEntity question = questionRepository.findByIdAndCurrentVersion(questionId).orElseThrow(() -> new NotFoundException("Question not found"));
        QuizVersionEntity newQuizVersion = quizVersionService.updateVersion(quiz);
        newQuizVersion.getQuestions().remove(question);
        quizVersionRepository.save(newQuizVersion);
    }

    public QuestionResponse changeQuestion(@Valid UUID quizId, @Valid UUID questionId, @Valid QuestionRequest questionRequest) {
        QuizEntity quiz = quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
        QuestionEntity questionEntity = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found"));
        QuizVersionEntity newQuizVersion = quizVersionService.updateVersion(quiz);
        newQuizVersion.getQuestions().remove(questionEntity);
        questionEntity = createQuestion(questionRequest, newQuizVersion);
        return QuestionMapper.INSTANCE.toDto(questionEntity, questionMapperFactory);
    }
}
