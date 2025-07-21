package gblas.books.backend.dto;

import gblas.books.backend.entity.ChapterEntity;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.validation.ValidChapterNumber;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record QuizRequest(
        List<QuestionRequest> questions
) { }

