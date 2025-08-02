package gblas.books.backend.repository;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.QuizAttemptEntity;

import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuizAttemptRepository extends CrudRepository<QuizAttemptEntity, UUID> {
    @Query("""
    SELECT qa FROM QuizAttemptEntity qa
    JOIN qa.quizVersion qv
    JOIN qv.quiz q
    JOIN q.chapter c
    JOIN c.book b
    JOIN b.owner u
    WHERE u = :user
    """)
    Page<QuizAttemptEntity> findByUser(UserEntity user, Pageable pageable);

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
    SELECT qa FROM QuizAttemptEntity qa
    JOIN qa.quizVersion qv
    JOIN qv.quiz q
    JOIN q.chapter c
    JOIN c.book b
    JOIN b.owner u
    WHERE u.id = :userId
    """)
    List<QuizAttemptEntity> findByUserId(UUID userId);

    @Query("""
    SELECT qa FROM QuizAttemptEntity qa
    JOIN qa.quizVersion qv
    JOIN qv.quiz q
    JOIN q.chapter c
    JOIN c.book b
    WHERE b.id = :bookId
    """)
    List<QuizAttemptEntity> findByBookId(UUID bookId);

    @Query("""
    SELECT qa FROM QuizAttemptEntity qa
    JOIN qa.quizVersion qv
    JOIN qv.quiz q
    WHERE q = :quiz
    """)
    List<QuizAttemptEntity> findByQuiz(QuizEntity quiz);

}

