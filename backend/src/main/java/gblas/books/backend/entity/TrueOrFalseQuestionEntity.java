package gblas.books.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Table(name = "true_or_false_questions")
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("TRUE_FALSE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrueOrFalseQuestionEntity extends QuestionEntity {
    private Boolean isAnswerTrue;
}
