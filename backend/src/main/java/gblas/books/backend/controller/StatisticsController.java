package gblas.books.backend.controller;

import gblas.books.backend.dto.statistics.BookStatisticsResponse;
import gblas.books.backend.dto.statistics.ChapterStatisticsResponse;
import gblas.books.backend.dto.statistics.UserStatisticsResponse;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/statistics")
@Tag(name = "Quiz attempt statistics", description = "APIs for retrieving user, book and chapter quiz statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "Get statistics from current user's quiz attempts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics successfully retrieved"),
            @ApiResponse(responseCode = "204", description = "User has no quiz attempts", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user must be authenticated", content = @Content)
    })
    @GetMapping("/user")
    public ResponseEntity<UserStatisticsResponse> getQuizAttemptsFromUser(@AuthenticationPrincipal UserEntity user) {
        UserStatisticsResponse response = statisticsService.getStatisticsFromUserAttempts(user);
        if (response == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get statistics for a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics successfully retrieved"),
            @ApiResponse(responseCode = "204", description = "Book has no quiz attempts", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookStatisticsResponse> getQuizAttemptsFromBook(@Valid @PathVariable UUID bookId) {
        BookStatisticsResponse response = statisticsService.getStatisticsFromBookAttempts(bookId);
        if (response == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get statistics for a specific chapter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics successfully retrieved"),
            @ApiResponse(responseCode = "204", description = "Chapter has a quiz but no attempts", content = @Content),
            @ApiResponse(responseCode = "404", description = "Chapter not found or does not have a quiz", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/chapters/{chapterId}")
    public ResponseEntity<ChapterStatisticsResponse> getQuizAttemptFromChapter(@Valid @PathVariable UUID chapterId) {
        ChapterStatisticsResponse response = statisticsService.getStatisticsFromOneChapterAttempts(chapterId);
        if (response == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }
}
