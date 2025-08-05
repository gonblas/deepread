package gblas.books.backend.dto.question.MultipleChoice;

import gblas.books.backend.dto.OptionResponse;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;

import java.util.List;
import java.util.UUID;

public record MultipleChoiceQuestionResponse(
        UUID id,
        QuestionEntity.QuestionType type,
        String prompt,
        String explanation,
        List<OptionResponse> options
) implements QuestionResponse {

}

