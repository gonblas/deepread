package gblas.books.backend.dto.question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gblas.books.backend.dto.question.OpenQuestion.UpdateOpenQuestionRequest;
import gblas.books.backend.dto.question.TrueOrFalse.UpdateTrueOrFalseQuestionRequest;
import gblas.books.backend.entity.question.QuestionEntity;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateOpenQuestionRequest.class, name = "OPEN"),
        @JsonSubTypes.Type(value = UpdateTrueOrFalseQuestionRequest.class, name = "TRUE_FALSE")
})
public interface UpdateQuestionRequest {
    QuestionEntity.QuestionType type();
    String prompt();
    String explanation();
}

