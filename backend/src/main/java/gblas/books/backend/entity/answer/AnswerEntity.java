package gblas.books.backend.entity.answer;

import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Table(name = "answers")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QuestionEntity question;

    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id")
    private QuizAttemptEntity quizAttempt;

    @Column(name = "type", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(nullable = true) //cambiar a false
    private Boolean isCorrect;
}


