package gblas.books.backend.entity;

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
    private String expectedAnswer;

}
