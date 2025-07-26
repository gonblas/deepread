package gblas.books.backend.dto;

import gblas.books.backend.dto.answer.AnswerResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record QuizAttemptResponse(
        @NotBlank UUID question_id,
        @NotEmpty(message = "Answer list cannot be empty")
        @Valid
        List<@Valid AnswerResponse> answers
) { }