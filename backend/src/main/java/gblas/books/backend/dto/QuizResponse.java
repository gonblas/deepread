package gblas.books.backend.dto;

import gblas.books.backend.entity.ChapterEntity;
import gblas.books.backend.validation.ValidChapterNumber;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record QuizResponse(
        @NotBlank UUID id,
        @NotBlank ChapterResponse chapter,
        List<QuestionResponse> questions
) { }
