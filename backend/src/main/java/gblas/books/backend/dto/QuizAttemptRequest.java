package gblas.books.backend.dto;

import gblas.books.backend.dto.answer.AnswerRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record QuizAttemptRequest(
        @NotEmpty(message = "Answer list cannot be empty")
        @Valid
        List<@Valid AnswerRequest> answers
) { }
