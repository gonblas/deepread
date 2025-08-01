package gblas.books.backend.controller;

import gblas.books.backend.dto.statistics.BookStatisticsResponse;
import gblas.books.backend.dto.statistics.ChapterStatisticsResponse;
import gblas.books.backend.dto.statistics.UserStatisticsResponse;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.service.StatisticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/statistics")
@Tag(name = "Quiz attempts staticstics", description = "APIs for managing quiz attempt statistics")
public class StatisticsController {
    private StatisticsService statisticsService;

    @GetMapping("/user")
    public UserStatisticsResponse getQuizAttemptsFromUser(@AuthenticationPrincipal UserEntity user) {
        return statisticsService.getStatisticsFromUserAttempts(user);
    }

    @GetMapping("/books/{bookId}")
    public BookStatisticsResponse getQuizAttemptsFromBook(@Valid @PathVariable UUID bookId) {
        return statisticsService.getStatisticsFromBookAttempts(bookId);
    }

    @GetMapping("/chapters/{chapterId}")
    public ChapterStatisticsResponse getQuizAttemptFromChapter(@Valid @PathVariable UUID chapterId) {
        return statisticsService.getStatisticsFromOneChapterAttempts(chapterId);
    }
}
