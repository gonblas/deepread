package gblas.books.backend.repository;
import gblas.books.backend.entity.BookEntity;

import gblas.books.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, UUID> {
    Page<BookEntity> findByOwner(UserEntity owner, Pageable pageable);
    Optional<BookEntity> findByOwnerAndId(UserEntity owner, UUID id);
    Page<BookEntity> findByOwnerAndGenreIn(UserEntity owner, List<BookEntity.BookGenre> genre, Pageable pageable);
}
