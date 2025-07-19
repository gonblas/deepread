package gblas.books.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "books")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    private String title;
    @ElementCollection
    private List<String> authors = new ArrayList<>();

    private String description;
    private BookGenre genre;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChapterEntity> chapters = new ArrayList<>();

    public enum BookGenre {
        ADVENTURE,
        SCIENCE_FICTION,
        FANTASY,
        MYSTERY,
        HORROR,
        ROMANCE,
        DRAMA,
        COMEDY,
        HISTORICAL_FICTION,
        DYSTOPIAN,
        CRIME,
        THRILLER,
        BIOGRAPHY,
        ESSAY,
        HISTORY,
        POLITICS,
        ECONOMICS,
        PHILOSOPHY,
        PSYCHOLOGY,
        NATURAL_SCIENCES,
        SOCIAL_SCIENCES,
        SELF_HELP,
        RELIGION,
        EDUCATION,
        TECHNOLOGY,
        HEALTH,
        BUSINESS,
        TRAVEL,
        COOKING,
        CHILDREN,
        YOUNG_ADULT,
        MIDDLE_GRADE,
        PICTURE_BOOKS,
        FAIRY_TALES,
        TEXTBOOK,
        SCIENTIFIC,
        PROGRAMMING,
        MEDICINE,
        MATHEMATICS,
        TRUE_CRIME,
        LITRPG,
        CYBERPUNK,
        HISTORICAL_ROMANCE
    }

}
