package gblas.books.backend.entity;

import gblas.books.backend.entity.question.QuestionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "quiz_versions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizEntity quiz;

    @Column(nullable = false)
    private Integer versionNumber = 1;

    @Column(nullable = false)
    private Boolean isCurrent = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "quiz_version_questions",
            joinColumns = @JoinColumn(name = "quiz_version_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<QuestionEntity> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quizVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAttemptEntity> attempts = new ArrayList<>();

    public void updateVersionNumber() {
        this.versionNumber++;
    }
}

