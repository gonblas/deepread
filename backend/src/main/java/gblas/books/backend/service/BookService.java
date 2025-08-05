package gblas.books.backend.service;

import gblas.books.backend.dto.BookRequest;
import gblas.books.backend.dto.BookResponse;
import gblas.books.backend.dto.BookUpdateRequest;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.BookEntity.BookGenre;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.BookMapper;
import gblas.books.backend.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Page<BookResponse> getBooks(UserEntity user, Pageable pageable, List<BookGenre> genres) {
        Page<BookEntity> books_page;
        if (genres == null || genres.isEmpty()) {
            books_page = bookRepository.findByOwner(user, pageable);
        } else {
            books_page = bookRepository.findByOwnerAndGenreIn(user, genres, pageable);
        }
        return books_page.map(BookMapper.INSTANCE::toDto);
    }

    public BookResponse addBook(UserEntity user, BookRequest bookRequest) {
        BookEntity bookEntity = BookMapper.INSTANCE.toEntity(bookRequest, user);
        bookRepository.save(bookEntity);
        return BookMapper.INSTANCE.toDto(bookEntity);
    }

    public void deleteBook(UserEntity user, UUID bookId) {
        BookEntity bookEntity = bookRepository.findByOwnerAndId(user, bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        bookRepository.delete(bookEntity);
    }

    public BookResponse changeBook(UserEntity user, UUID bookId, BookRequest bookRequest) {
        BookEntity bookEntity = bookRepository.findByOwnerAndId(user, bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        BookMapper.INSTANCE.changeEntity(bookRequest, bookEntity);
        bookRepository.save(bookEntity);
        return BookMapper.INSTANCE.toDto(bookEntity);
    }

    public BookResponse updateBook(UserEntity user, UUID bookId, BookUpdateRequest bookRequest) {
        BookEntity bookEntity = bookRepository.findByOwnerAndId(user, bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        BookMapper.INSTANCE.updateEntity(bookRequest, bookEntity);
        bookRepository.save(bookEntity);
        return BookMapper.INSTANCE.toDto(bookEntity);
    }

}
