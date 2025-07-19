package gblas.books.backend.repository;
import gblas.books.backend.entity.ChapterEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChapterRepository extends CrudRepository<ChapterEntity, UUID> {
}
