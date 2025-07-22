package gblas.books.backend.dto;

import gblas.books.backend.entity.QuestionEntity.QuestionType;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "question_type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenQuestionRequest.class, name = "OPEN"),
        @JsonSubTypes.Type(value = TrueOrFalseQuestionRequest.class, name = "TRUE_FALSE")
})
public interface QuestionRequest {
    QuestionType question_type();
    String prompt();
    String explanation();
}
