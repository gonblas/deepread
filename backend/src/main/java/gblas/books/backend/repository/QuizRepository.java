package gblas.books.backend.repository;
import gblas.books.backend.entity.ChapterEntity;
import gblas.books.backend.entity.QuizEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends CrudRepository<QuizEntity, UUID> {
    Optional<QuizEntity> findByChapter(ChapterEntity chapter);

    @Query("""
    SELECT q FROM QuizEntity q
    JOIN q.chapter c
    JOIN c.book b
    JOIN b.owner u
    WHERE u.id = :userId
    """)
    Page<QuizEntity> findAllQuizzesByUserId(UUID userId, Pageable pageable);

    @Query("""
    SELECT q FROM QuizEntity q
    JOIN q.chapter c
    JOIN c.book b
    WHERE b.id = :bookId
    """)
    Page<QuizEntity> findAllQuizzesByBookId(UUID bookId, Pageable pageable);
}

