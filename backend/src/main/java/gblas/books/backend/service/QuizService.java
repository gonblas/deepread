package gblas.books.backend.service;

import gblas.books.backend.dto.QuizRequest;
import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.entity.*;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.QuizMapper;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
import gblas.books.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class QuizService {

    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final QuizVersionRepository quizVersionRepository;
    private final QuestionMapperFactory questionMapperFactory;
    private final QuizVersionService quizVersionService;


    public Page<QuizResponse> getAllQuizzesFromUser(UserEntity user, Pageable pageable) {
        Page<QuizEntity> quizzesPage = quizRepository.findAllQuizzesByUser(user, pageable);
        return quizzesPage.map(quiz -> {
            QuizVersionEntity currentVersion = quizVersionService.getLastQuizVersionEntity(quiz);
            return QuizMapper.INSTANCE.toDto(currentVersion, questionMapperFactory);
        });
    }

    public Page<QuizResponse> getAllQuizzesFromBook(UUID bookId, Pageable pageable) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        Page<QuizEntity> quizzesPage = quizRepository.findAllQuizzesByBook(book, pageable);
        return quizzesPage.map(quiz -> {
            QuizVersionEntity currentVersion = quizVersionService.getLastQuizVersionEntity(quiz);
            return QuizMapper.INSTANCE.toDto(currentVersion, questionMapperFactory);
        });
    }

    public QuizResponse getQuizFromChapter(UUID bookId, UUID chapterId) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        ChapterEntity chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));

        if (!chapter.getBook().getId().equals(book.getId())) {
            throw new NotFoundException("Chapter does not belong to this book");
        }

        QuizEntity quiz = quizRepository.findByChapter(chapter).orElseThrow(() -> new NotFoundException("Quiz not found"));
        QuizVersionEntity currentVersion = quizVersionService.getLastQuizVersionEntity(quiz);

        return QuizMapper.INSTANCE.toDto(currentVersion, questionMapperFactory);
    }

    @Transactional
    public QuizResponse addQuiz(UUID bookId, UUID chapterId, QuizRequest quizRequest) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        ChapterEntity chapterEntity = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new NotFoundException("Chapter not found"));

        if(!chapterEntity.getBook().getId().equals(bookEntity.getId())) {
            throw new NotFoundException("Chapter does not belong to this book");
        }

        Optional<QuizEntity> quizEntity = quizRepository.findByChapter(chapterEntity);
        if(quizEntity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Quiz already exists for this chapter");
        }

        return getNewQuizResponse(quizRequest, chapterEntity);
    }

    @Transactional
    public void deleteQuiz(UUID bookId, UUID chapterId) {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        ChapterEntity chapterEntity = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));
        QuizEntity quizEntity = quizRepository.findByChapter(chapterEntity).orElseThrow(() -> new NotFoundException("Quiz not found"));

        if (!chapterEntity.getBook().getId().equals(bookEntity.getId())) {
            throw new NotFoundException("Chapter does not belong to this book");
        }

        chapterEntity.setQuiz(null);
        quizEntity.setChapter(null);
        quizRepository.delete(quizEntity);
    }

    @Transactional
    protected QuizResponse getNewQuizResponse(QuizRequest quizRequest, ChapterEntity chapterEntity) {
        QuizEntity newQuizEntity = new QuizEntity();
        chapterEntity.setQuizBidirectional(newQuizEntity);

        quizRepository.save(newQuizEntity);

        QuizVersionEntity firstVersion = new QuizVersionEntity();
        firstVersion.setQuiz(newQuizEntity);
        firstVersion.setIsCurrent(true);
        quizVersionRepository.save(firstVersion);

        List<QuestionEntity> questions = quizRequest.questions().stream()
                .map(request -> {
                    return questionService.createQuestion(request, firstVersion);
                })
                .toList();
        firstVersion.getQuestions().addAll(questions);
        newQuizEntity.getQuestions().addAll(questions);
        newQuizEntity.getVersions().add(firstVersion);
        return QuizMapper.INSTANCE.toDto(firstVersion, questionMapperFactory);
    }
}
