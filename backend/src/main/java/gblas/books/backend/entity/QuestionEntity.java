package gblas.books.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "questions")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "question_type")
public abstract class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private QuestionType type;

    @Column(nullable = false)
    private String prompt;

    @Column(nullable = true)
    private String explanation;

    public enum QuestionType {
        TRUE_FALSE,
        OPEN
    }
}

