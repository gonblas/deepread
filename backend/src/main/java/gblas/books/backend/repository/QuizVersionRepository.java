package gblas.books.backend.repository;

import gblas.books.backend.entity.QuizVersionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizVersionRepository extends CrudRepository<QuizVersionEntity, UUID> {

    @Query("SELECT v FROM QuizVersionEntity v WHERE v.quiz.id = :quizId AND v.isCurrent = true")
    Optional<QuizVersionEntity> findCurrentVersionByQuizId(@Param("quizId") UUID quizId);

}