package gblas.books.backend.repository;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.ChapterEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChapterRepository extends CrudRepository<ChapterEntity, UUID> {
    Page<ChapterEntity> findByBook(BookEntity book, Pageable pageable);
    Optional<ChapterEntity> findById(UUID chapterId);
    ChapterEntity findByBookAndNumber(BookEntity book, int number);
}
