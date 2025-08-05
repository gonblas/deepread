package gblas.books.backend.repository;

import gblas.books.backend.entity.question.QuestionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends CrudRepository<QuestionEntity, UUID> {
    Optional<QuestionEntity> getById(UUID id);

    @Query("""
    SELECT q FROM QuestionEntity q
    JOIN q.versions v
    WHERE q.id = :questionId AND v.isCurrent = true
""")
    Optional<QuestionEntity> findByIdAndCurrentVersion(UUID questionId);

}
