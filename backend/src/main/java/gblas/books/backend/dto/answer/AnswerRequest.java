package gblas.books.backend.dto.answer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gblas.books.backend.dto.answer.MultipleChoice.MultipleChoiceAnswerRequest;
import gblas.books.backend.dto.answer.Open.OpenAnswerRequest;
import gblas.books.backend.dto.answer.TrueOrFalse.TrueOrFalseAnswerRequest;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenAnswerRequest.class, name = "OPEN"),
        @JsonSubTypes.Type(value = TrueOrFalseAnswerRequest.class, name = "TRUE_FALSE"),
        @JsonSubTypes.Type(value = MultipleChoiceAnswerRequest.class, name = "MULTIPLE_CHOICE")
})
public interface AnswerRequest {
    QuestionType type();
    UUID questionId();
}