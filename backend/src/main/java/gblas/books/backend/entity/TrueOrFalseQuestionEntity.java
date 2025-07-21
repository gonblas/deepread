package gblas.books.backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("TRUE_FALSE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrueOrFalseQuestionEntity extends QuestionEntity {
    private Boolean isAnswerTrue;

}
