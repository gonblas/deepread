package gblas.books.backend.dto.answer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gblas.books.backend.dto.answer.OpenAnswer.OpenAnswerRequest;
import gblas.books.backend.dto.answer.TrueOrFalse.TrueOrFalseAnswerRequest;

import java.util.UUID;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrueOrFalseAnswerRequest.class, name = "TRUE_FALSE"),
        @JsonSubTypes.Type(value = OpenAnswerRequest.class, name = "OPEN")
})
public abstract class AnswerRequest {
    private UUID questionId;
}
