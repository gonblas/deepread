package gblas.books.backend.controller;

import gblas.books.backend.dto.ChapterRequest;
import gblas.books.backend.dto.ChapterResponse;
import gblas.books.backend.dto.UpdateChapterRequest;
import gblas.books.backend.service.ChapterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/book/{bookId}/chapters")
@Tag(name = "Chapter Management", description = "APIs for managing users")
public class ChapterController {
    private ChapterService chapterService;

    @GetMapping
    public Page<ChapterResponse> getChapters(@Valid @PathVariable UUID bookId, Pageable pageable) {
        return chapterService.getChapters(bookId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChapterResponse createChapter(@Valid @PathVariable UUID bookId, @Valid @RequestBody ChapterRequest chapterRequest) {
        return chapterService.addChapter(bookId, chapterRequest);
    }

    @DeleteMapping("/{chapterId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
        chapterService.deleteChapter(bookId, chapterId);
    }

    @PutMapping("/{chapterId}")
    public ChapterResponse changeChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody ChapterRequest chapterRequest) {
        return chapterService.changeChapter(bookId, chapterId, chapterRequest);
    }

    @PatchMapping("/{chapterId}")
    public ChapterResponse updateChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody UpdateChapterRequest chapterRequest) {
        return chapterService.updateChapter(bookId, chapterId, chapterRequest);
    }
}
