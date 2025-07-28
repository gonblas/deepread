package gblas.books.backend.service;

import gblas.books.backend.dto.QuizAttemptRequest;
import gblas.books.backend.dto.QuizAttemptResponse;
import gblas.books.backend.dto.QuizRequest;
import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.entity.*;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.QuizAttemptMapper;
import gblas.books.backend.mapper.answer.AnswerMapperFactory;
import gblas.books.backend.repository.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class QuizAttemptService {

    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final QuizAttemptRepository quizAttemptRepository;
    private final AnswerMapperFactory answerMapperFactory;

    public Page<QuizAttemptResponse> getQuizAttemptsFromUser(UserEntity user, Pageable pageable) {
        Page<QuizAttemptEntity> attempts_page = quizAttemptRepository.findByUserId(user.getId(), pageable);
        return attempts_page.map(quizAttempt -> QuizAttemptMapper.INSTANCE.toDto(quizAttempt, answerMapperFactory));
    }

    public Page<QuizAttemptResponse> getQuizAttemptsFromBook(UUID bookId, Pageable pageable) {
        Page<QuizAttemptEntity> attempts_page = quizAttemptRepository.findByBookId(bookId, pageable);
        return attempts_page.map(quizAttempt -> QuizAttemptMapper.INSTANCE.toDto(quizAttempt, answerMapperFactory));
    }

    public Page<QuizAttemptResponse> getQuizAttemptFromChapter(UUID bookId, UUID chapterId, Pageable pageable) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        ChapterEntity chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));

        if(!chapter.getBook().getId().equals(book.getId())) {
            throw new NotFoundException("Chapter does not belong to this book");
        }

        QuizEntity quiz = quizRepository.findByChapter(chapter).orElseThrow(() -> new NotFoundException("Quiz not found"));
        Page<QuizAttemptEntity> attempts_page = quizAttemptRepository.findByQuiz(quiz, pageable);
        return attempts_page.map(quizAttempt -> QuizAttemptMapper.INSTANCE.toDto(quizAttempt, answerMapperFactory));
    }

//    public QuizResponse createQuizAttempt(@Valid UUID quizId, @Valid QuizAttemptRequest quizAttemptRequest) {
//        QuizEntity quiz =  quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found"));
//        QuizAttemptEntity quizAttempt = QuizAttemptMapper.INSTANCE.toEntity(quizAttemptRequest, answerMapperFactory);
//        return QuizAttemptMapper.INSTANCE.toDto(quizAttempt, answerMapperFactory);
//    }
//
//    public QuizResponse addQuiz(UUID bookId, UUID chapterId, QuizRequest quizRequest) {
//        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
//
//        ChapterEntity chapterEntity = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));
//        Optional<QuizEntity> quizEntity = quizRepository.findByChapter(chapterEntity);
//
//        if(quizEntity.isPresent()) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Quiz already exists for this chapter");
//        }
//        if(!chapterEntity.getBook().getId().equals(bookEntity.getId())) {
//            throw new NotFoundException("Chapter does not belong to this book");
//        }
//
//        QuizEntity newQuizEntity = new QuizEntity();
//        chapterEntity.setQuizBidirectional(newQuizEntity);
//        return getQuizResponse(quizRequest, newQuizEntity);
//    }
//
//    public void deleteQuiz(UUID bookId, UUID chapterId) {
//        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
//        ChapterEntity chapterEntity = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));
//        QuizEntity quizEntity = quizRepository.findByChapter(chapterEntity).orElseThrow(() -> new NotFoundException("Quiz not found"));
//
//        if (!chapterEntity.getBook().getId().equals(bookEntity.getId())) {
//            throw new NotFoundException("Chapter does not belong to this book");
//        }
//
//        chapterEntity.setQuiz(null);
//        quizEntity.setChapter(null);
//
//        quizRepository.delete(quizEntity);
//    }
//
//    private QuizResponse getQuizResponse(QuizRequest quizRequest, QuizEntity quiz) {
//        quizRepository.save(quiz);
//        List<QuestionEntity> questions = quizRequest.questions().stream()
//                .map(request -> {
//                    return questionService.createQuestion(request, quiz);
//                })
//                .toList();
//
//        quiz.setQuestions(questions);
//        return QuizMapper.INSTANCE.toDto(quiz, questionFactory);
//    }

}
