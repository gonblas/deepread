package gblas.books.backend.controller;

import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.ChapterEntity;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.repository.BookRepository;
import gblas.books.backend.repository.ChapterRepository;
import gblas.books.backend.repository.UserRepository;
import gblas.books.backend.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static gblas.books.backend.util.RepositoryAssertions.assertCountEquals;
import static gblas.books.backend.util.RepositoryAssertions.assertIsEmpty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ChapterControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private ChapterRepository chapterRepository;
    @Autowired private JwtService jwtService;

    private String authToken;
    private UserEntity user;
    private BookEntity book;
    private ChapterEntity chapter;
    private ChapterEntity anotherChapter;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setHashedPassword("encoded_password");
        user = userRepository.save(user);

        book = new BookEntity();
        book.setTitle("Test Book");
        book.setOwner(user);
        book = bookRepository.save(book);

        chapter = new ChapterEntity();
        chapter.setNumber(1);
        chapter.setTitle("Chapter 1");
        chapter.setSummary("Content 1");
        chapter.setBook(book);
        chapter = chapterRepository.save(chapter);

        anotherChapter = new ChapterEntity();
        anotherChapter.setNumber(2);
        anotherChapter.setTitle("Chapter 2");
        anotherChapter.setSummary("Content 2");
        anotherChapter.setBook(book);
        anotherChapter = chapterRepository.save(anotherChapter);

        authToken = jwtService.generateToken(user.getEmail());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        bookRepository.deleteAll();
        chapterRepository.deleteAll();
    }

    @Test
    void addChapter_withValidData_shouldCreateChapter() throws Exception {
        Integer expectedChapterNumber = 3;
        String expectedTitle = "Second Chapter";
        String requestBody = String.format("""
        {
            "title": "%s",
            "number": %d
        }
        """, expectedTitle, expectedChapterNumber);

        mockMvc.perform(post("/api/book/" + book.getId() + "/chapters")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.number").value(expectedChapterNumber));
        assertCountEquals(chapterRepository, 3);
    }

    @Test
    void addChapter_withDuplicateChapterNumber_shouldReturnConflict() throws Exception {
        int duplicateNumber = chapter.getNumber();
        String requestBody = String.format("""
        {
            "number": %d,
            "title": "Duplicate"
        }
    """, duplicateNumber);

        mockMvc.perform(post("/api/book/" + book.getId() + "/chapters")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Chapter number already exists for this book"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void addChapter_missingChapterNumber_shouldReturnBadRequest() throws Exception {
        String requestBody = """
        {
            "title": "No number"
        }
    """;

        mockMvc.perform(post("/api/book/" + book.getId() + "/chapters")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.number").value("Chapter number is required"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void addChapter_missingTitle_shouldReturnBadRequest() throws Exception {
        String requestBody = """
        {
            "chapterNumber": 3
        }
    """;

        mockMvc.perform(post("/api/book/" + book.getId() + "/chapters")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void addChapter_withoutBody_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/book/" + book.getId() + "/chapters")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is missing or malformed"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void addChapter_withInvalidTitle_shouldReturnBadRequest() throws Exception {
        final String invalidTitle = " ";

        String requestBody = String.format("""
        {
            "title": "%s",
            "summary": "Texto válido"
        }
        """, invalidTitle);

        mockMvc.perform(post("/api/book/" + book.getId() + "/chapters")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void updateChapter_withValidData_shouldUpdateChapter() throws Exception {
        String updatedTitle = "Another Title";

        String request = String.format("""
        {
            "title": "%s"
        }
    """, updatedTitle);

        mockMvc.perform(patch("/api/book/" + book.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chapter.getId().toString()))
                .andExpect(jsonPath("$.title").value(updatedTitle))
                .andExpect(jsonPath("$.summary").value(chapter.getSummary()))
                .andExpect(jsonPath("$.number").value(chapter.getNumber()));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void updateChapter_withSameChapterNumber_shouldAllow() throws Exception {
        int sameNumber = chapter.getNumber();

        String requestBody = String.format("""
        {
            "number": %d
        }
    """, sameNumber);

        mockMvc.perform(patch("/api/book/" + book.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(sameNumber))
                .andExpect(jsonPath("$.id").value(chapter.getId().toString()))
                .andExpect(jsonPath("$.title").value(chapter.getTitle()))
                .andExpect(jsonPath("$.summary").value(chapter.getSummary()));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void updateChapter_withDuplicateChapterNumber_shouldReturnConflict() throws Exception {
        int duplicateNumber = anotherChapter.getNumber();

        String requestBody = String.format("""
        {
            "number": %d
        }
    """, duplicateNumber);

        mockMvc.perform(patch("/api/book/" + book.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Chapter number already exists for this book"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void updateChapter_withEmptyTitle_shouldReturnBadRequest() throws Exception {
        String requestBody = """
        {
            "title": " "
        }
    """;

        mockMvc.perform(patch("/api/book/" + book.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Field must not be null or blank if present"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void replaceChapter_withValidData_shouldReplaceChapter() throws Exception {
        String updatedTitle = "Título Reemplazado";
        Integer updatedNumber = 3;
        String updatedSummary = "Resumen reemplazado";

        String request = String.format("""
        {
            "title": "%s",
            "number": %d,
            "summary": "%s"
        }
    """, updatedTitle, updatedNumber, updatedSummary);

        mockMvc.perform(put("/api/book/" + book.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedTitle))
                .andExpect(jsonPath("$.number").value(updatedNumber))
                .andExpect(jsonPath("$.summary").value(updatedSummary));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void replaceChapter_withMissingFields_shouldReturnBadRequest() throws Exception {
        String request = """
        {
            "summary": "Falta el título"
        }
    """;

        mockMvc.perform(put("/api/book/" + book.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void replaceChapter_withDuplicateNumber_shouldReturnConflict() throws Exception {
        String conflictRequest = """
        {
            "title": "New",
            "summary": "New",
            "number": 2
        }
    """;

        mockMvc.perform(put("/api/book/" + book.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conflictRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Chapter number already exists for this book"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void replaceChapter_withSameNumber_shouldReturnConflict() throws Exception {
        String request = """
        {
            "title": "Updated",
            "summary": "Same Number",
            "number": 1
        }
    """;

        mockMvc.perform(put("/api/book/" + book.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Chapter number already exists for this book"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void deleteChapter_withValidId_shouldDeleteChapter() throws Exception {
        mockMvc.perform(delete("/api/book/" + book.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
        assertCountEquals(chapterRepository, 1);
    }

    @Test
    void deleteChapter_withInvalidChapterId_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/book/" + book.getId() + "/chapters/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Chapter not found"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void deleteChapter_withInvalidBookId_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/book/" + UUID.randomUUID() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
        assertCountEquals(chapterRepository, 2);
    }

    @Test
    void deleteChapter_withChapterNotBelongingToBook_shouldReturnNotFound() throws Exception {
        BookEntity otherBook = new BookEntity();
        otherBook.setTitle("Other Book");
        otherBook.setOwner(user);
        otherBook = bookRepository.save(otherBook);

        mockMvc.perform(delete("/api/book/" + otherBook.getId() + "/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Chapter does not belong to this book"));
        assertCountEquals(chapterRepository, 2);
    }
}
