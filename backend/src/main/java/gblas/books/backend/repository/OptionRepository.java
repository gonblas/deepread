package gblas.books.backend.repository;

import gblas.books.backend.entity.OptionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OptionRepository extends CrudRepository<OptionEntity, UUID> {
}
