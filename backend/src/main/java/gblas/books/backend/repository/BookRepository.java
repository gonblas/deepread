package gblas.books.backend.repository;

import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, UUID> {
    Optional<BookEntity> findByOwnerAndId(UserEntity owner, UUID id);

    @Query("""
        SELECT b FROM BookEntity b
        WHERE b.owner = :owner
        AND (:genres IS NULL OR b.genre IN :genres)
        AND (LOWER(b.title) LIKE LOWER(CONCAT('%',:substring,'%')) OR :substring IS NULL)
    """)
    Page<BookEntity> findByOwnerAndOptionalGenreAndTitle(
            @Param("owner") UserEntity owner,
            @Param("genres") List<BookEntity.BookGenre> genres,
            @Param("substring") String substring,
            Pageable pageable);


}
