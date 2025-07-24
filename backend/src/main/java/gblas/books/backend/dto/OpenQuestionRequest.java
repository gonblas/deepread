package gblas.books.backend.dto;

import gblas.books.backend.entity.QuestionEntity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OpenQuestionRequest(
        @NotNull QuestionType question_type,
        @NotBlank String prompt,
        String explanation,
        @NotBlank String expectedAnswer
) implements QuestionRequest {

}
