package gblas.books.backend.entity.answer;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "true_false_answers")
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("TRUE_FALSE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@OnDelete(action = OnDeleteAction.CASCADE)
public class TrueOrFalseAnswerEntity extends AnswerEntity {
    private Boolean answer;
}
