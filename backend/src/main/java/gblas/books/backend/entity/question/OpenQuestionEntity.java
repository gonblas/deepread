package gblas.books.backend.entity.question;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.Open.OpenAnswerRequest;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.answer.OpenAnswerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "open_questions")
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("OPEN")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenQuestionEntity extends QuestionEntity {
    @Column(nullable = false)
    private String expectedAnswer;

    @Override
    public Boolean validate(AnswerRequest request) {
        if(request instanceof OpenAnswerRequest openAnswerRequest) {
            return openAnswerRequest.answerText().equals(expectedAnswer);
        }
        throw new IllegalArgumentException("Answer must be of type OpenAnswerRequest");
    }

    @Override
    public Boolean validate(AnswerEntity request) {
        if(request instanceof OpenAnswerEntity openAnswerEntity) {
            return openAnswerEntity.getAnswerText().equals(expectedAnswer);
        }
        throw new IllegalArgumentException("Answer must be of type OpenAnswerEntity");
    }
}
