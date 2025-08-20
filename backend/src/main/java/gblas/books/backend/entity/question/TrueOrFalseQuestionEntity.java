package gblas.books.backend.entity.question;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.TrueOrFalse.TrueOrFalseAnswerRequest;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.answer.TrueOrFalseAnswerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Table(name = "true_or_false_questions")
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("TRUE_FALSE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TrueOrFalseQuestionEntity extends QuestionEntity {
    @Column(nullable = false)
    private Boolean isAnswerTrue;

    @Override
    public Boolean validate(AnswerRequest request) {
        if(request instanceof TrueOrFalseAnswerRequest trueOrFalseAnswerRequest) {
            return trueOrFalseAnswerRequest.answer() == isAnswerTrue;
        }
        throw new IllegalArgumentException("Answer must be of type TrueOrFalseAnswerRequest");
    }

    @Override
    public Boolean validate(AnswerEntity request) {
        if(request instanceof TrueOrFalseAnswerEntity trueOrFalseAnswerEntity) {
            return trueOrFalseAnswerEntity.getAnswer() == isAnswerTrue;
        }
        throw new IllegalArgumentException("Answer must be of type TrueOrFalseAnswerEntity");
    }
}
