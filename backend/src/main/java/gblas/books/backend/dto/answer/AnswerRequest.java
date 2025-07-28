package gblas.books.backend.dto.answer;

import gblas.books.backend.dto.answer.OpenAnswer.OpenAnswerRequest;
import gblas.books.backend.dto.answer.TrueOrFalse.TrueOrFalseAnswerRequest;
import gblas.books.backend.entity.question.QuestionEntity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrueOrFalseAnswerRequest.class, name = "TRUE_FALSE"),
        @JsonSubTypes.Type(value = OpenAnswerRequest.class, name = "OPEN")
})
public interface AnswerRequest {
    UUID questionId();
    QuestionEntity.QuestionType type();
    Boolean isCorrect();
}