package gblas.books.backend.dto.statistics;

public record DailyStats(
        String date,
        int attempts,
        double averageScore
) {}

