package gblas.books.backend.repository;
import gblas.books.backend.entity.question.QuestionEntity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepository extends CrudRepository<QuestionEntity, UUID> {
    QuestionEntity getById(UUID id);

    @Modifying
    @Query("""
        DELETE FROM QuestionEntity q
        WHERE q.id IN (
            SELECT q2.id FROM QuizVersionEntity v JOIN v.questions q2 WHERE v.quiz.id = :quizId
        )
    """)
    void deleteQuestionsByQuizId(@Param("quizId") UUID quizId);

}
