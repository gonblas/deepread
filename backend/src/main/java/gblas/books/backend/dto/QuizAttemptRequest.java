package gblas.books.backend.dto;

import gblas.books.backend.dto.answer.AnswerRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.List;

public record QuizAttemptRequest(
        @PastOrPresent @NotNull LocalDateTime startedAt,
        @NotEmpty(message = "Answer list cannot be empty")
        @Valid
        List<@Valid AnswerRequest> answers
) { }
