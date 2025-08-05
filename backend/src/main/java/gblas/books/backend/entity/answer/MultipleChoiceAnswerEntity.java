package gblas.books.backend.entity.answer;

import gblas.books.backend.entity.OptionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Table(name = "multiple_choice_answers")
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@OnDelete(action = OnDeleteAction.CASCADE)
public class MultipleChoiceAnswerEntity extends AnswerEntity {

    @ManyToMany
    @JoinTable(
            name = "multiple_choice_answer_options",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<OptionEntity> optionsSelected;
}
