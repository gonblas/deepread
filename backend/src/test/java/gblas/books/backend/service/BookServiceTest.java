package gblas.books.backend.service;

import gblas.books.backend.dto.*;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.BookEntity.BookGenre;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.BookMapper;
import gblas.books.backend.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock private BookRepository bookRepository;
    @InjectMocks private BookService underTest;
    private UserEntity user;
    private BookEntity bookEntity;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");

        bookId = UUID.randomUUID();
        bookEntity = new BookEntity();
        bookEntity.setId(bookId);
        bookEntity.setOwner(user);
        bookEntity.setTitle("Test Book");
    }

    @Test
    void getBooks_shouldReturnBooks_whenGenresAreNull() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findByOwner(user, pageable))
                .thenReturn(new PageImpl<>(List.of(bookEntity)));

        Page<BookResponse> result = underTest.getBooks(user, pageable, null);

        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findByOwner(user, pageable);
    }

    @Test
    void getBooks_shouldReturnBooks_whenGenresArePresent() {
        Pageable pageable = PageRequest.of(0, 10);
        List<BookEntity.BookGenre> genres = List.of(BookGenre.ADVENTURE);

        when(bookRepository.findByOwnerAndGenreIn(user, genres, pageable))
                .thenReturn(new PageImpl<>(List.of(bookEntity)));

        Page<BookResponse> result = underTest.getBooks(user, pageable, genres);

        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findByOwnerAndGenreIn(user, genres, pageable);
    }


    @Test
    void shouldAddBook() {
        BookRequest request = new BookRequest("Test Book", "Author", BookGenre.ADVENTURE, null);
        BookEntity newBook = BookMapper.INSTANCE.toEntity(request, user);

        BookResponse response = underTest.addBook(user, request);

        verify(bookRepository).save(any());
        assertEquals("Test Book", response.title());
    }

    @Test
    void shouldDeleteBook() {
        when(bookRepository.findByOwnerAndId(user, bookId))
                .thenReturn(Optional.of(bookEntity));

        underTest.deleteBook(user, bookId);

        verify(bookRepository).delete(bookEntity);
    }

    @Test
    void shouldThrow_whenDeleteBookNotFound() {
        when(bookRepository.findByOwnerAndId(user, bookId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> underTest.deleteBook(user, bookId));
    }

    @Test
    void shouldChangeBook() {
        BookRequest request = new BookRequest("New Title", "New Author", BookGenre.ADVENTURE, null);

        when(bookRepository.findByOwnerAndId(user, bookId))
                .thenReturn(Optional.of(bookEntity));

        BookResponse response = underTest.changeBook(user, bookId, request);

        verify(bookRepository).save(bookEntity);
        assertEquals("New Title", response.title());
    }

    @Test
    void shouldUpdateBook() {
        BookUpdateRequest request = new BookUpdateRequest("Updated Title", null, null, null);

        when(bookRepository.findByOwnerAndId(user, bookId))
                .thenReturn(Optional.of(bookEntity));

        BookResponse response = underTest.updateBook(user, bookId, request);

        verify(bookRepository).save(bookEntity);
        assertEquals("Updated Title", response.title());
    }

    @Test
    void shouldThrow_whenChangeBookNotFound() {
        when(bookRepository.findByOwnerAndId(user, bookId)).thenReturn(Optional.empty());
        BookRequest request = new BookRequest("X", "Y", BookGenre.ADVENTURE, null);
        assertThrows(NotFoundException.class, () -> underTest.changeBook(user, bookId, request));
    }

    @Test
    void shouldThrow_whenUpdateBookNotFound() {
        when(bookRepository.findByOwnerAndId(user, bookId)).thenReturn(Optional.empty());
        BookUpdateRequest request = new BookUpdateRequest("X", null, null, null);
        assertThrows(NotFoundException.class, () -> underTest.updateBook(user, bookId, request));
    }

}
