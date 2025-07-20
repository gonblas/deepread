package gblas.books.backend.service;

import gblas.books.backend.dto.ChapterRequest;
import gblas.books.backend.dto.ChapterResponse;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.ChapterEntity;
import gblas.books.backend.exceptions.NotFoundException;
import gblas.books.backend.exceptions.ResourceAlreadyExistsException;
import gblas.books.backend.mapper.ChapterMapper;
import gblas.books.backend.repository.BookRepository;
import gblas.books.backend.repository.ChapterRepository;
import gblas.books.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ChapterService {

    private UserRepository userRepository;
    private ChapterRepository chapterRepository;
    private BookRepository bookRepository;

    public Page<ChapterResponse> getChapters(UUID bookId, Pageable pageable) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        Page<ChapterEntity> chapters_page = chapterRepository.findByBook(book, pageable);

        return chapters_page.map(ChapterMapper::dtoFrom);
    }

    public ChapterResponse addChapter(UUID bookId, ChapterRequest chapterRequest) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        ChapterEntity chapterEntity = chapterRepository.findByBookAndNumber(book, chapterRequest.number());

        if(chapterEntity != null ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chapter number already exists for this book");
        }
        ChapterEntity newChapterEntity = new ChapterEntity();
        newChapterEntity.setBook(book);

        return getChapterResponse(chapterRequest, newChapterEntity);
    }

    public void deleteChapter(UUID bookId, UUID chapterId) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        ChapterEntity chapterEntity = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));
        if(!chapterEntity.getBook().getId().equals(book.getId())) {
            throw new NotFoundException("Chapter does not belong to this book");
        }
        chapterRepository.delete(chapterEntity);
    }

    public ChapterResponse updateChapter(UUID bookId, UUID chapterId, ChapterRequest chapterRequest) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        ChapterEntity chapterEntity = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));

        if(chapterEntity == null ) {
            throw new NotFoundException("Chapter not found");
        }

        return getChapterResponse(chapterRequest, chapterEntity);
    }

    private ChapterResponse getChapterResponse(ChapterRequest chapterRequest, ChapterEntity chapter) {
        chapter.setTitle(chapterRequest.title());
        chapter.setNumber(chapterRequest.number());
        if(chapterRequest.summary() != null) {
            chapter.setSummary(chapterRequest.summary());
        }
        chapterRepository.save(chapter);
        return ChapterMapper.dtoFrom(chapter);
    }
}
