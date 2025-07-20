package gblas.books.backend.service;

import gblas.books.backend.dto.BookResponse;
import gblas.books.backend.dto.BookRequest;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.BookEntity.*;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.BookMapper;
import gblas.books.backend.repository.BookRepository;
import gblas.books.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public Page<BookResponse> getBooks(String email, Pageable pageable, List<BookGenre> genres) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow();
        Page<BookEntity> books_page;
        if (genres == null || genres.isEmpty()) {
            books_page = bookRepository.findByOwner(user, pageable);
        } else {
            books_page = bookRepository.findByOwnerAndGenreIn(user, genres, pageable);
        }
        return books_page.map(BookMapper::dtoFrom);
    }

    public BookResponse addBook(String email, BookRequest bookRequest) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow();

        BookEntity bookEntity = new BookEntity();
        bookEntity.setOwner(user);
        return getBookResponse(bookRequest, bookEntity);
    }

    public void deleteBook(String email, UUID bookId) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        BookEntity bookEntity = bookRepository.findByOwnerAndId(user, bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        bookRepository.delete(bookEntity);
    }

    public BookResponse updateBook(String email, UUID bookId, BookRequest bookRequest) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        BookEntity bookEntity = bookRepository.findByOwnerAndId(user, bookId).orElseThrow(() -> new NotFoundException("Book not found"));

        return getBookResponse(bookRequest, bookEntity);
    }

    private BookResponse getBookResponse(BookRequest bookRequest, BookEntity bookEntity) {
        bookEntity.setTitle(bookRequest.title());
        bookEntity.setDescription(bookRequest.description());
        if(bookRequest.authors()  != null && !bookRequest.authors().isEmpty()) {
            bookEntity.setAuthors(bookRequest.authors());
        }
        bookEntity.setGenre(bookRequest.genre());

        bookRepository.save(bookEntity);
        return BookMapper.dtoFrom(bookEntity);
    }


}
