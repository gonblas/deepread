package gblas.books.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecentQuizAttemptResponse(
        UUID id,
        String bookTitle,
        int chapterNumber,
        int score,
        String submittedAt
) {}
