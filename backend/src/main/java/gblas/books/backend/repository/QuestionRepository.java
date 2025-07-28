package gblas.books.backend.repository;
import gblas.books.backend.entity.question.QuestionEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepository extends CrudRepository<QuestionEntity, UUID> {
    QuestionEntity getById(UUID id);
}
