package gblas.books.backend.dto.statistics;

import java.time.LocalDate;

public record DailyStats(
        String date,
        int attempts,
        double averageScore
) {}

