package gblas.books.backend.dto.question.MultipleChoice;

import gblas.books.backend.dto.OptionRequest;
import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MultipleChoiceQuestionRequest(
        @NotNull QuestionEntity.QuestionType type,
        @NotBlank String prompt,
        String explanation,
        @NotEmpty List<OptionRequest> options
) implements QuestionRequest {

}