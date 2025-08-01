package gblas.books.backend.dto.statistics;

import java.util.List;

public record GeneralStatistics(
        int totalAttempts,
        int totalQuizzesAttempted,
        double averageScore,
        double bestScore,
        double worstScore,
        double averageTimeSeconds,
        List<DailyStats> dailyStatsTimeline
) {}
