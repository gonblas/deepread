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
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id")
    private QuizEntity quiz;

    @Column(nullable = false)
    private String questionStatement;

    @Column(nullable = false)
    private String answer;

}
