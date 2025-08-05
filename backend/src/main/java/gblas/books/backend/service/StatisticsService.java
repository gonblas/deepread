package gblas.books.backend.service;

import gblas.books.backend.dto.statistics.*;
import gblas.books.backend.entity.*;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.repository.BookRepository;
import gblas.books.backend.repository.ChapterRepository;
import gblas.books.backend.repository.QuizAttemptRepository;
import gblas.books.backend.repository.QuizRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;
    private final ChapterRepository chapterRepository;
    private final BookRepository bookRepository;
    private final Pageable emptyPageable = Pageable.unpaged();

    public UserStatisticsResponse getStatisticsFromUserAttempts(UserEntity user) {
        List<QuizAttemptEntity> attempts = quizAttemptRepository.findByUser(user, emptyPageable).getContent();
        if (attempts.isEmpty()) {
            return null;
        }

        GeneralStatistics userStats = calculateGeneralStatistics(attempts);

        Map<UUID, List<QuizAttemptEntity>> attemptsByBook = attempts.stream()
                .collect(Collectors.groupingBy(a -> a.getQuizVersion().getQuiz().getChapter().getBook().getId()));

        List<BookStatisticsSummary> books = attemptsByBook.entrySet().stream()
                .map(entry -> {
                    UUID bookId = entry.getKey();
                    List<QuizAttemptEntity> bookAttempts = entry.getValue();

                    String bookTitle = bookAttempts.getFirst().getQuizVersion().getQuiz().getChapter().getBook().getTitle();

                    GeneralStatistics bookStats = calculateGeneralStatistics(bookAttempts);

                    return new BookStatisticsSummary(bookId, bookTitle, bookStats);
                })
                .toList();

        return new UserStatisticsResponse(userStats, books);
    }

    public BookStatisticsResponse getStatisticsFromBookAttempts(UUID bookId) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        List<QuizAttemptEntity> attempts = quizAttemptRepository.findByBook(book, emptyPageable).getContent();
        if (attempts.isEmpty()) {
            return null;
        }

        GeneralStatistics bookStats = calculateGeneralStatistics(attempts);

        Map<UUID, List<QuizAttemptEntity>> attemptsByChapter = attempts.stream()
                .collect(Collectors.groupingBy(a -> a.getQuizVersion().getQuiz().getChapter().getId()));

        List<ChapterStatisticsResponse> chapters = attemptsByChapter.entrySet().stream()
                .map(entry -> {
                    UUID chapterId = entry.getKey();
                    List<QuizAttemptEntity> chapterAttempts = entry.getValue();

                    String chapterTitle = chapterAttempts.getFirst().getQuizVersion().getQuiz().getChapter().getTitle();

                    GeneralStatistics chapterStats = calculateGeneralStatistics(chapterAttempts);

                    return new ChapterStatisticsResponse(chapterId, chapterTitle, chapterStats);
                })
                .toList();

        return new BookStatisticsResponse(bookStats, chapters);
    }

    public ChapterStatisticsResponse getStatisticsFromOneChapterAttempts(UUID chapterId) {
        ChapterEntity chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));
        if(chapter.getQuiz() == null) {
            return null;
        }
        QuizEntity quiz = chapter.getQuiz();
        List<QuizAttemptEntity> attempts = quizAttemptRepository.findByQuiz(quiz, emptyPageable).getContent();
        if(attempts.isEmpty()) {
            return null;
        }
        return new ChapterStatisticsResponse(
                quiz.getChapter().getId(),
                quiz.getChapter().getTitle(),
                calculateGeneralStatistics(attempts)
        );
    }

    private GeneralStatistics calculateGeneralStatistics(List<QuizAttemptEntity> attempts) {
        int totalAttempts = attempts.size();

        Set<UUID> uniqueQuizIds = attempts.stream()
                .map(a -> a.getQuizVersion().getQuiz().getId())
                .collect(Collectors.toSet());

        int totalQuizzesAttempted = uniqueQuizIds.size();

        DoubleSummaryStatistics scoreStats = attempts.stream()
                .mapToDouble(QuizAttemptEntity::getCorrectCount)
                .summaryStatistics();

        double averageTimeSeconds = attempts.stream()
                .filter(a -> a.getStartedAt() != null && a.getSubmittedAt() != null)
                .mapToLong(a -> a.getSubmittedAt().toEpochSecond(ZoneOffset.UTC) - a.getStartedAt().toEpochSecond(ZoneOffset.UTC))
                .average()
                .orElse(0);

        Map<LocalDate, List<QuizAttemptEntity>> groupedByDate = attempts.stream()
                .filter(a -> a.getSubmittedAt() != null)
                .collect(Collectors.groupingBy(a -> a.getSubmittedAt().atZone(ZoneOffset.UTC).toLocalDate()));

        List<DailyStats> dailyStatsTimeline = groupedByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<QuizAttemptEntity> attemptsOnDate = entry.getValue();

                    int attemptsCount = attemptsOnDate.size();

                    double avgScore = attemptsOnDate.stream()
                            .mapToDouble(QuizAttemptEntity::getCorrectCount)
                            .average()
                            .orElse(0);

                    return new DailyStats(date.toString(), attemptsCount, avgScore);
                })
                .sorted(Comparator.comparing(DailyStats::date))
                .toList();

        return new GeneralStatistics(
                totalAttempts,
                totalQuizzesAttempted,
                scoreStats.getAverage(),
                scoreStats.getMax(),
                scoreStats.getMin(),
                averageTimeSeconds,
                dailyStatsTimeline
        );
    }

}



