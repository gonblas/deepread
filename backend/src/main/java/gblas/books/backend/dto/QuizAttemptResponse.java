package gblas.books.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gblas.books.backend.dto.answer.AnswerResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record QuizAttemptResponse(
        @NotBlank UUID id,
        @NotBlank UUID quiz_id,
        @NotEmpty(message = "Answer list cannot be empty")
        @Valid
        List<@Valid AnswerResponse> answers,
        @NotBlank Integer correctCount,
        @JsonFormat(shape = JsonFormat.Shape.STRING) @NotBlank Instant startedAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING) @NotBlank Instant submittedAt
) { }