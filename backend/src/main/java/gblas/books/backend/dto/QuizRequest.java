package gblas.books.backend.dto;

import gblas.books.backend.entity.ChapterEntity;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.validation.ValidChapterNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record QuizRequest(
        @NotEmpty(message = "Questions list cannot be empty")
        @Valid List<QuestionRequest> questions
) { }

