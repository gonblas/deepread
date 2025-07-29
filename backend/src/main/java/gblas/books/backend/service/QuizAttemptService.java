package gblas.books.backend.service;

import gblas.books.backend.dto.QuizAttemptRequest;
import gblas.books.backend.dto.QuizAttemptResponse;
import gblas.books.backend.dto.QuizRequest;
import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.entity.*;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.QuizAttemptMapper;
import gblas.books.backend.mapper.answer.AnswerMapper;
import gblas.books.backend.mapper.answer.AnswerMapperFactory;
import gblas.books.backend.mapper.question.QuestionMapper;
import gblas.books.backend.repository.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class QuizAttemptService {

//    private final BookRepository bookRepository;
//    private final ChapterRepository chapterRepository;
//    private final QuizRepository quizRepository;
//    private final QuestionRepository questionRepository;
//    private final QuizAttemptRepository quizAttemptRepository;
//    private final AnswerMapperFactory answerMapperFactory;
//    private final AnswerRepository answerRepository;
//    private final QuizVersionService quizVersionService;
//
//    public Page<QuizAttemptResponse> getQuizAttemptsFromUser(UserEntity user, Pageable pageable) {
//        Page<QuizAttemptEntity> attempts_page = quizAttemptRepository.findByUserId(user.getId(), pageable);
//        return attempts_page.map(quizAttempt -> QuizAttemptMapper.INSTANCE.toDto(quizAttempt, answerMapperFactory));
//    }
//
//    public Page<QuizAttemptResponse> getQuizAttemptsFromBook(UUID bookId, Pageable pageable) {
//        Page<QuizAttemptEntity> attempts_page = quizAttemptRepository.findByBookId(bookId, pageable);
//        return attempts_page.map(quizAttempt -> QuizAttemptMapper.INSTANCE.toDto(quizAttempt, answerMapperFactory));
//    }
//
//    public Page<QuizAttemptResponse> getQuizAttemptFromChapter(UUID bookId, UUID chapterId, Pageable pageable) {
//        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
//        ChapterEntity chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));
//
//        if(!chapter.getBook().getId().equals(book.getId())) {
//            throw new NotFoundException("Chapter does not belong to this book");
//        }
//
//        QuizEntity quiz = quizRepository.findByChapter(chapter).orElseThrow(() -> new NotFoundException("Quiz attempt not found"));
//        Page<QuizAttemptEntity> attempts_page = quizAttemptRepository.findByQuiz(quiz, pageable);
//        return attempts_page.map(quizAttempt -> QuizAttemptMapper.INSTANCE.toDto(quizAttempt, answerMapperFactory));
//    }
//
//    public QuizAttemptResponse createQuizAttempt(@Valid UUID quizId, @Valid QuizAttemptRequest quizAttemptRequest) {
//        QuizEntity quiz =  quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz attempt not found"));
//        QuizAttemptEntity newQuizAttemptEntity = new QuizAttemptEntity();
//        newQuizAttemptEntity.setQuizVersion(quizVersionService.getLastQuizVersionEntity(quiz));
//        return getQuizResponse(quizAttemptRequest, newQuizAttemptEntity);
//    }
//
//    private QuizAttemptResponse getQuizResponse(QuizAttemptRequest quizAttemptRequest, QuizAttemptEntity quizAttempt) {
//        quizAttempt.setStartedAt(quizAttemptRequest.startedAt());
//        quizAttempt.setSubmittedAt(LocalDateTime.now());
//        quizAttemptRepository.save(quizAttempt);
//        List<AnswerEntity> answers = quizAttemptRequest.answers().stream()
//                .map(request -> {
//                    QuestionEntity question = questionRepository.getById(request.questionId());
//                    AnswerEntity newAnswer = AnswerMapper.INSTANCE.toEntity(request, quizAttempt, question, answerMapperFactory);
//                    answerRepository.save(newAnswer);
//                    return newAnswer;
//                })
//                .toList();
//        quizAttempt.setAnswers(answers);
//        quizAttempt.setCorrectCount(getCorrectCountFromAnswers(answers));
//        return QuizAttemptMapper.INSTANCE.toDto(quizAttempt, answerMapperFactory);
//    }
//
//    private Integer getCorrectCountFromAnswers(List<AnswerEntity> answers) {
//        return (int) answers.stream()
//                .filter(AnswerEntity::getIsCorrect)
//                .count();
//    }
//
//    public void deleteQuizAttempt(@Valid UUID quizAttemptId) {
//        QuizAttemptEntity quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(() -> new NotFoundException("Quiz attempt not found"));
//        quizAttemptRepository.delete(quizAttempt);
//    }
}
