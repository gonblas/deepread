package gblas.books.backend.service;

import gblas.books.backend.dto.ChapterRequest;
import gblas.books.backend.dto.ChapterResponse;
import gblas.books.backend.dto.UpdateChapterRequest;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.ChapterEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.mapper.ChapterMapper;
import gblas.books.backend.repository.BookRepository;
import gblas.books.backend.repository.ChapterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ChapterServiceTest {

    @Mock private ChapterRepository chapterRepository;
    @Mock private BookRepository bookRepository;

    @InjectMocks private ChapterService chapterService;

    @Test
    void shouldReturnChapters_whenBookExists() {
        UUID bookId = UUID.randomUUID();
        BookEntity book = new BookEntity();
        Page<ChapterEntity> chapterPage = new PageImpl<>(List.of(new ChapterEntity()));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(chapterRepository.findByBook(eq(book), any(Pageable.class))).thenReturn(chapterPage);

        Page<ChapterResponse> result = chapterService.getChapters(bookId, Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldThrowNotFound_whenBookDoesNotExist_getChapters() {
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> chapterService.getChapters(bookId, Pageable.unpaged()));
    }

    @Test
    void shouldAddChapter_whenValidRequest() {
        UUID bookId = UUID.randomUUID();
        ChapterRequest req = new ChapterRequest("title", 1, "");
        BookEntity book = new BookEntity();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(chapterRepository.findByBookAndNumber(book, 1)).thenReturn(Optional.empty());

        ChapterResponse res = chapterService.addChapter(bookId, req);
        assertEquals("title", res.title());
    }

    @Test
    void shouldThrowConflict_whenChapterNumberExists_addChapter() {
        UUID bookId = UUID.randomUUID();
        BookEntity book = new BookEntity();
        ChapterRequest req = new ChapterRequest("title", 1, "");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(chapterRepository.findByBookAndNumber(book, 1)).thenReturn(Optional.of(new ChapterEntity()));

        assertThrows(ResponseStatusException.class, () -> chapterService.addChapter(bookId, req));
    }

    @Test
    void shouldDeleteChapter_whenValidRequest() {
        UUID bookId = UUID.randomUUID();
        UUID chapterId = UUID.randomUUID();
        BookEntity book = new BookEntity();
        book.setId(bookId);
        ChapterEntity chapter = new ChapterEntity();
        chapter.setId(chapterId);
        chapter.setBook(book);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));

        chapterService.deleteChapter(bookId, chapterId);
        verify(chapterRepository).delete(chapter);
    }

    @Test
    void shouldThrowNotFound_whenChapterNotInBook_deleteChapter() {
        UUID bookId = UUID.randomUUID();
        UUID chapterId = UUID.randomUUID();
        BookEntity book = new BookEntity();
        book.setId(bookId);
        ChapterEntity chapter = new ChapterEntity();
        chapter.setId(chapterId);
        chapter.setBook(new BookEntity());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));

        assertThrows(NotFoundException.class, () -> chapterService.deleteChapter(bookId, chapterId));
    }

    @Test
    void shouldChangeChapter_whenValidRequest() {
        UUID bookId = UUID.randomUUID();
        UUID chapterId = UUID.randomUUID();
        ChapterRequest req = new ChapterRequest("newTitle", 2, "");

        BookEntity book = new BookEntity();
        ChapterEntity chapter = new ChapterEntity();
        chapter.setId(chapterId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(chapterRepository.findByBookAndNumber(book, 2)).thenReturn(Optional.empty());

        ChapterResponse response = chapterService.changeChapter(bookId, chapterId, req);
        assertEquals("newTitle", response.title());
    }

    @Test
    void shouldThrowConflict_whenSameNumberExists_changeChapter() {
        UUID bookId = UUID.randomUUID();
        UUID chapterId = UUID.randomUUID();
        ChapterRequest req = new ChapterRequest("newTitle", 2, "");

        BookEntity book = new BookEntity();
        ChapterEntity chapter = new ChapterEntity();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(chapterRepository.findByBookAndNumber(book, 2)).thenReturn(Optional.of(new ChapterEntity()));

        assertThrows(ResponseStatusException.class, () -> chapterService.changeChapter(bookId, chapterId, req));
    }

    @Test
    void shouldUpdateChapter_whenPartialUpdate() {
        UUID bookId = UUID.randomUUID();
        UUID chapterId = UUID.randomUUID();
        UpdateChapterRequest req = new UpdateChapterRequest("updated", null, "");
        ChapterEntity chapter = new ChapterEntity();

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));

        ChapterResponse response = chapterService.updateChapter(bookId, chapterId, req);
        assertEquals("updated", response.title());
    }
}
