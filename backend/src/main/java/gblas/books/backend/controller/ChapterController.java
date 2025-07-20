package gblas.books.backend.controller;

import gblas.books.backend.dto.ChapterRequest;
import gblas.books.backend.dto.ChapterResponse;
import gblas.books.backend.service.ChapterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/book/{bookId}/chapters")
public class ChapterController {
    private ChapterService chapterService;

    @GetMapping
    public ResponseEntity<Page<ChapterResponse>> getChapters(@Valid @PathVariable UUID bookId, Pageable pageable) {
        Page<ChapterResponse> chapters = chapterService.getChapters(bookId, pageable);
        return ResponseEntity.ok(chapters);
    }

    @PostMapping
    public ResponseEntity<ChapterResponse> createChapter(@Valid @PathVariable UUID bookId, @Valid @RequestBody ChapterRequest chapterRequest) {
        ChapterResponse chapter = chapterService.addChapter(bookId, chapterRequest);
        return ResponseEntity.ok(chapter);
    }

    @DeleteMapping("/{chapterId}")
    public ResponseEntity<?> deleteChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
        chapterService.deleteChapter(bookId, chapterId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{chapterId}")
    public ResponseEntity<ChapterResponse> updateChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody ChapterRequest chapterRequest) {
        ChapterResponse chapter = chapterService.updateChapter(bookId, chapterId, chapterRequest);
        return ResponseEntity.ok(chapter);
    }
}
