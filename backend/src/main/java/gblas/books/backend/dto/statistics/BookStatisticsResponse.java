package gblas.books.backend.dto.statistics;

import java.util.List;

public record BookStatisticsResponse(
        GeneralStatistics stats,
        List<ChapterStatisticsResponse> chapters
) {}
