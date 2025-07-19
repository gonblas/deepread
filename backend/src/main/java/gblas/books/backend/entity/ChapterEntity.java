package gblas.books.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "chapters")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private int number;
    @Column(nullable = false)
    private String title;

    @Lob
    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @OneToOne(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private QuizEntity quiz;
}

