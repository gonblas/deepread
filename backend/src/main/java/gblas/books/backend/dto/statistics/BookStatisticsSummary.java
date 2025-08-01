package gblas.books.backend.dto.statistics;

import java.util.UUID;

public record BookStatisticsSummary(
        UUID bookId,
        String title,
        GeneralStatistics stats
) {}
