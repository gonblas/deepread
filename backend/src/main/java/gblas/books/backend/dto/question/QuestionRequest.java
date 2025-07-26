package gblas.books.backend.dto.question;

import gblas.books.backend.dto.question.TrueOrFalse.TrueOrFalseQuestionRequest;
import gblas.books.backend.dto.question.OpenQuestion.OpenQuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenQuestionRequest.class, name = "OPEN"),
        @JsonSubTypes.Type(value = TrueOrFalseQuestionRequest.class, name = "TRUE_FALSE")
})
public interface QuestionRequest {
    QuestionType type();
    String prompt();
    String explanation();
}
