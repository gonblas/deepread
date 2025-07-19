package gblas.books.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "quizzes")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "chapter_id", nullable = false, unique = true)
    private ChapterEntity chapter;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions = new ArrayList<>();
}
