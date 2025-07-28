package gblas.books.backend.repository;
import gblas.books.backend.entity.QuizAttemptEntity;

import gblas.books.backend.entity.QuizEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuizAttemptRepository extends CrudRepository<QuizAttemptEntity, UUID> {
    @Query("""
    SELECT q FROM QuizAttemptEntity qa
    JOIN qa.quiz q
    JOIN q.chapter c
    JOIN c.book b
    JOIN b.owner u
    WHERE u.id = :userId
    """)
    Page<QuizAttemptEntity> findByUserId(UUID userId, Pageable pageable);

    @Query("""
    SELECT q FROM QuizAttemptEntity qa
    JOIN qa.quiz q
    JOIN q.chapter c
    JOIN c.book b
    WHERE b.id = :bookId
    """)
    Page<QuizAttemptEntity> findByBookId(UUID bookId, Pageable pageable);

    Page<QuizAttemptEntity> findByQuiz(QuizEntity quiz, Pageable pageable);
}

