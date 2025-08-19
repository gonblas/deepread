package gblas.books.backend.repository;

import gblas.books.backend.dto.RecentQuizAttemptResponse;
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

import java.time.Instant;
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
            @Param("start") Instant start,
            @Param("end") Instant end,
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

    @Query("""
        SELECT new gblas.books.backend.dto.RecentQuizAttemptResponse(
            qa.id,
            b.title,
            c.number,
            qa.correctCount,
            TO_CHAR(qa.submittedAt, 'YYYY-MM-DD"T"HH24:MI:SS') AS submittedAt
        )
        FROM QuizAttemptEntity qa
        JOIN qa.quizVersion qv
        JOIN qv.quiz q
        JOIN q.chapter c
        JOIN c.book b
        WHERE b.owner = :user
        ORDER BY qa.submittedAt DESC
    """)
    Page<RecentQuizAttemptResponse> findRecentAttemptsByUser(
            @Param("user") UserEntity user,
            Pageable pageable
    );

    @Query("""
        SELECT new gblas.books.backend.dto.RecentQuizAttemptResponse(
            qa.id,
            b.title,
            c.number,
            qa.correctCount,
            TO_CHAR(qa.submittedAt, 'YYYY-MM-DD"T"HH24:MI:SS') AS submittedAt
        )
        FROM QuizAttemptEntity qa
        JOIN qa.quizVersion qv
        JOIN qv.quiz q
        JOIN q.chapter c
        JOIN c.book b
        WHERE b.id = :book_id
        ORDER BY qa.submittedAt DESC
    """)
    Page<RecentQuizAttemptResponse> findRecentAttemptsByBook(
            @Param("book_id") UUID bookId,
            Pageable pageable
    );

    @Query("""
        SELECT new gblas.books.backend.dto.RecentQuizAttemptResponse(
            qa.id,
            b.title,
            c.number,
            qa.correctCount,
            TO_CHAR(qa.submittedAt, 'YYYY-MM-DD"T"HH24:MI:SS') AS submittedAt
        )
        FROM QuizAttemptEntity qa
        JOIN qa.quizVersion qv
        JOIN qv.quiz q
        JOIN q.chapter c
        JOIN c.book b
        WHERE q.id = :quiz_id
        ORDER BY qa.submittedAt DESC
    """)
    Page<RecentQuizAttemptResponse> findRecentAttemptsByQuiz( @Param("quiz_id") UUID quizId, Pageable pageable);
}

