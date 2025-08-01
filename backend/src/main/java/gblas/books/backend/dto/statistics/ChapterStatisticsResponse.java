package gblas.books.backend.dto.statistics;

import java.util.UUID;

public record ChapterStatisticsResponse(
        UUID chapterId,
        String chapterTitle,
        GeneralStatistics stats
) {}

