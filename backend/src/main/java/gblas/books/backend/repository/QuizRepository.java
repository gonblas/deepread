package gblas.books.backend.repository;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.ChapterEntity;
import gblas.books.backend.entity.QuizEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends CrudRepository<QuizEntity, UUID> {
    Optional<QuizEntity> findByChapter(BookEntity chapter);
}

