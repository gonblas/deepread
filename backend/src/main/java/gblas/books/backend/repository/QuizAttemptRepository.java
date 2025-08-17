package gblas.books.backend.repository;

import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface QuizAttemptRepository extends CrudRepository<QuizAttemptEntity, UUID> {
    @Query(""" 
    SELECT qa 
    FROM QuizAttemptEntity qa
    JOIN qa.quizVersion qv 
    JOIN qv.quiz q 
    JOIN q.chapter c 
    JOIN c.book b 
    JOIN b.owner u 
    WHERE u = :user 
    """)
    Page<QuizAttemptEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("""
    SELECT qa
    FROM QuizAttemptEntity qa
    JOIN qa.quizVersion qv
    JOIN qv.quiz q
    JOIN q.chapter c
    JOIN c.book b
    JOIN b.owner u
    WHERE u = :user
      AND (qa.submittedAt >= COALESCE(:start, qa.submittedAt))
      AND (qa.submittedAt <= COALESCE(:end, qa.submittedAt))
""")
    Page<QuizAttemptEntity> findByUserAndSubmittedAtBetween(
            @Param("user") UserEntity user,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );



    @Query("""
    SELECT qa FROM QuizAttemptEntity qa
    JOIN qa.quizVersion qv
    JOIN qv.quiz q
    JOIN q.chapter c
    JOIN c.book b
    WHERE b = :book
    """)
    Page<QuizAttemptEntity> findByBook(BookEntity book, Pageable pageable);

    @Query("""
    SELECT qa FROM QuizAttemptEntity qa
    JOIN qa.quizVersion qv
    JOIN qv.quiz q
    WHERE q = :quiz
    """)
    Page<QuizAttemptEntity> findByQuiz(QuizEntity quiz, Pageable pageable);

}

