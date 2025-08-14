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
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ChapterService {

    private ChapterRepository chapterRepository;
    private BookRepository bookRepository;

    public Page<ChapterResponse> getChapters(UUID bookId, Pageable pageable) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        Page<ChapterEntity> chapters_page = chapterRepository.findByBook(book, pageable);

        return chapters_page.map(ChapterMapper.INSTANCE::toDto);
    }

    public ChapterResponse getChapterDetails(@Valid UUID bookId, @Valid UUID chapterId) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        ChapterEntity chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));
        return ChapterMapper.INSTANCE.toDto(chapter);
    }

    public ChapterResponse addChapter(UUID bookId, ChapterRequest chapterRequest) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        Optional<ChapterEntity> chapterEntity = chapterRepository.findByBookAndNumber(book, chapterRequest.number());

        if(chapterEntity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chapter number already exists for this book");
        }

        ChapterEntity newChapterEntity = ChapterMapper.INSTANCE.toEntity(chapterRequest, book);
        chapterRepository.save(newChapterEntity);
        return ChapterMapper.INSTANCE.toDto(newChapterEntity);
    }

    public void deleteChapter(UUID bookId, UUID chapterId) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        ChapterEntity chapterEntity = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));
        if(!chapterEntity.getBook().getId().equals(book.getId())) {
            throw new NotFoundException("Chapter does not belong to this book");
        }
        chapterRepository.delete(chapterEntity);
    }

    public ChapterResponse changeChapter(UUID bookId, UUID chapterId, ChapterRequest chapterRequest) {
        ChapterEntity chapterToChange = findChapterWithSameNumber(bookId, chapterId, chapterRequest.number());
        ChapterMapper.INSTANCE.changeEntity(chapterRequest, chapterToChange);
        chapterRepository.save(chapterToChange);
        return ChapterMapper.INSTANCE.toDto(chapterToChange);
    }

    public ChapterResponse updateChapter(UUID bookId, UUID chapterId, UpdateChapterRequest chapterRequest) {
        ChapterEntity chapterToChange = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));
        if (chapterRequest.number() != null && chapterRequest.number() != chapterToChange.getNumber()) {
            chapterToChange = findChapterWithSameNumber(bookId, chapterId, chapterRequest.number());
        }

        ChapterMapper.INSTANCE.updateEntity(chapterRequest, chapterToChange);
        chapterRepository.save(chapterToChange);
        return ChapterMapper.INSTANCE.toDto(chapterToChange);
    }

    private ChapterEntity findChapterWithSameNumber(UUID bookId, UUID chapterId, Integer number) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        ChapterEntity chapterEntity = chapterRepository.findById(chapterId).orElseThrow(() -> new NotFoundException("Chapter not found"));

        Optional<ChapterEntity> chapter = chapterRepository
                .findByBookAndNumber(book, number);
        boolean thereIsADifferentChapterWithSameChapter = chapter.isPresent() && !(chapterEntity.getNumber() == chapter.get().getNumber());
        if(thereIsADifferentChapterWithSameChapter) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chapter number already exists for this book");
        }

        return chapterEntity;
    }

}
