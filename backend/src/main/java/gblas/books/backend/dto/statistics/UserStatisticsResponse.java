package gblas.books.backend.dto.statistics;

import java.util.List;

public record UserStatisticsResponse(
        GeneralStatistics stats,
        List<BookStatisticsSummary> books
) {}

