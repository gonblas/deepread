package gblas.books.backend.controller;

import gblas.books.backend.dto.ChapterRequest;
import gblas.books.backend.dto.ChapterResponse;
import gblas.books.backend.dto.UpdateChapterRequest;
import gblas.books.backend.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/book/{bookId}/chapters")
@Tag(name = "Chapter Management", description = "APIs for managing chapters within books")
public class ChapterController {
    private ChapterService chapterService;

    @Operation(
            summary = "Get paginated list of chapters",
            description = "Retrieves a pageable list of chapters for a given book.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Chapters retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book not found - no response body", content = @Content)
            }
    )
    @GetMapping
    public Page<ChapterResponse> getChapters(@Valid @PathVariable UUID bookId, Pageable pageable) {
        return chapterService.getChapters(bookId, pageable);
    }

    @Operation(
            summary = "Get chapter details",
            description = "Retrieves a set of details from chapter for a given id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Chapters retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book or Chapter not found - no response body", content = @Content)
            }
    )
    @GetMapping("/{chapterId}")
    public ChapterResponse getChapterDetails(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, Pageable pageable) {
        return chapterService.getChapterDetails(bookId, chapterId, pageable);
    }

    @Operation(
            summary = "Create a new chapter",
            description = "Adds a new chapter to the specified book.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Chapter created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid chapter data or book ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book not found - no response body", content = @Content)
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChapterResponse createChapter(@Valid @PathVariable UUID bookId, @Valid @RequestBody ChapterRequest chapterRequest) {
        return chapterService.addChapter(bookId, chapterRequest);
    }

    @Operation(
            summary = "Delete a chapter",
            description = "Deletes the specified chapter from the given book.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Chapter deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid book or chapter ID - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book or chapter not found - no response body", content = @Content)
            }
    )
    @DeleteMapping("/{chapterId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId) {
        chapterService.deleteChapter(bookId, chapterId);
    }

    @Operation(
            summary = "Fully update a chapter",
            description = "Replaces the chapter data with the provided information.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Chapter updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid chapter data or IDs - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book or chapter not found - no response body", content = @Content)
            }
    )
    @PutMapping("/{chapterId}")
    public ChapterResponse changeChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody ChapterRequest chapterRequest) {
        return chapterService.changeChapter(bookId, chapterId, chapterRequest);
    }

    @Operation(
            summary = "Partially update a chapter",
            description = "Updates some fields of the chapter with the provided information.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Chapter partially updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid chapter data or IDs - no response body", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book or chapter not found - no response body", content = @Content)
            }
    )
    @PatchMapping("/{chapterId}")
    public ChapterResponse updateChapter(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID chapterId, @Valid @RequestBody UpdateChapterRequest chapterRequest) {
        return chapterService.updateChapter(bookId, chapterId, chapterRequest);
    }
}
