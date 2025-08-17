package gblas.books.backend.entity;

import gblas.books.backend.entity.answer.AnswerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "quiz_attempts")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuizAttemptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private QuizVersionEntity quizVersion;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AnswerEntity> answers = new ArrayList<>();

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(name = "correct_count")
    private Integer correctCount;

    @PostLoad
    public void setCorrectCountFromAnswers() {
        if (answers == null) {
            correctCount = 0;
            return;
        }

        correctCount = (int) answers.stream()
                .filter(AnswerEntity::getIsCorrect)
                .count();
    }
}

