package gblas.books.backend.entity.answer;

import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.question.QuestionEntity.QuestionType;
import gblas.books.backend.entity.QuizEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private QuestionEntity question;

    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id")
    private QuizAttemptEntity quizAttempt;

    @Transient
    private QuestionType type;

    @Column(nullable = false)
    private Boolean isCorrect;
}


