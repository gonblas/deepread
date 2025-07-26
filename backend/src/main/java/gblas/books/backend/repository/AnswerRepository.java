package gblas.books.backend.repository;

import gblas.books.backend.entity.answer.AnswerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnswerRepository extends CrudRepository<AnswerEntity, UUID> {
}


