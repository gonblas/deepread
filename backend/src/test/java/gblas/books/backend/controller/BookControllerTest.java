package gblas.books.backend.controller;

import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.repository.BookRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static gblas.books.backend.util.RepositoryAssertions.assertCountEquals;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private JwtService jwtService;

    private String authToken;
    private BookEntity book;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setHashedPassword("encoded_password");
        user = userRepository.save(user);

        book = new BookEntity();
        book.setTitle("Test Book");
        book.setOwner(user);
        book = bookRepository.save(book);

        authToken = jwtService.generateToken(user.getEmail());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    void getBooks_withBooksSaved_returnAPageNotEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content").isNotEmpty());
        assertNotNull(bookRepository.findById(book.getId()));
    }

    @Test
    void getBooks_withoutToken_returnAnUnauthorizedError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addBook_withValidData_shouldAddBook() throws Exception {
        final String expectedTitle = "Test Book";
        final String expectedGenre = "ECONOMICS";
        final String expectedAuthor = "Book Author";
        final String expectedDescription = "";

        String requestBody = String.format("""
        {
            "title": "%s",
            "genre": "%s",
            "description": "%s",
            "authors": ["%s"]
        }
    """, expectedTitle, expectedGenre, expectedDescription, expectedAuthor);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.genre").value(expectedGenre))
                .andExpect(jsonPath("$.description").value(expectedDescription))
                .andExpect(jsonPath("$.authors[0]").value(expectedAuthor));
        assertCountEquals(bookRepository, 2);
    }

    @Test
    void addBook_withValidDataMissingToken_shouldReturnAnUnauthorizedRequest() throws Exception {
        final String expectedTitle = "Test Book";
        final String expectedGenre = "ECONOMICS";
        final String expectedAuthor = "Book Author";
        final String expectedDescription = "";

        String requestBody = String.format("""
        {
            "title": "%s",
            "genre": "%s",
            "description": "%s",
            "authors": ["%s"]
        }
    """, expectedTitle, expectedGenre, expectedDescription, expectedAuthor);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void addBook_withoutBodyRequest_shouldReturnAnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/book")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Request body is missing or malformed"));
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void addBook_withInvalidTitle_shouldReturnABadRequest() throws Exception {
        final String invalidTitle = " ";

        String requestBody = String.format("""
        {
            "title": "%s"
        }
    """, invalidTitle);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void addBook_withInvalidGenre_shouldABadRequest() throws Exception {
        final String validTitle = "Valid title";
        final String invalidGenre = "Invalid genre";

        String requestBody = String.format("""
        {
            "title": "%s",
            "genre": "%s"
        }
    """, validTitle, invalidGenre);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid value for field: genre"));
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void deleteBook_withValidData_shouldDeleteBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book/" +  book.getId())
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_withInvalidBookId_shouldReturnABadRequest() throws Exception {
        String wrongId = book.getId() + "wrong part";
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book/" +  wrongId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid UUID format: " + wrongId));
    }

    @Test
    void deleteBook_withFakeId_shouldReturnABadRequest() throws Exception {
        String fakeId = UUID.randomUUID().toString();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book/" +  fakeId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void deleteBook_withoutToken_shouldReturnAUnauthorizedRequest() throws Exception {
        String fakeId = UUID.randomUUID().toString();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book/" +  fakeId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changeBook_withValidData_shouldReplaceBook() throws Exception {
        String newTitle = "New Title";
        String newDescription = "New Description";
        String newGenre = "ECONOMICS";
        String newAuthor = "Updated Author";

        String requestBody = String.format("""
        {
            "title": "%s",
            "genre": "%s",
            "description": "%s",
            "authors": ["%s"]
        }
        """, newTitle, newGenre, newDescription, newAuthor);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/" + book.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(newTitle))
                .andExpect(jsonPath("$.description").value(newDescription))
                .andExpect(jsonPath("$.genre").value(newGenre))
                .andExpect(jsonPath("$.authors[0]").value(newAuthor));
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void changeBook_withInvalidTitle_shouldReturnBadRequest() throws Exception {
        String invalidTitle = " ";

        String requestBody = String.format("""
        {
            "title": "%s",
            "genre": "ECONOMICS",
            "description": "Valid",
            "authors": ["Author"]
        }
        """, invalidTitle);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/" + book.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void changeBook_withoutBody_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/" + book.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is missing or malformed"));
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void changeBook_withoutToken_shouldReturnUnauthorized() throws Exception {
        String validTitle = "Any";
        String requestBody = String.format("""
        {
            "title": "%s",
            "genre": "ECONOMICS",
            "description": "Test",
            "authors": ["A"]
        }
        """, validTitle);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void updateBook_withDescriptionOnly_shouldUpdateDescription() throws Exception {
        String newDescription = "Only description updated";

        String requestBody = String.format("""
        {
            "description": "%s"
        }
        """, newDescription);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/book/" + book.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId().toString()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.description").value(newDescription))
                .andExpect(jsonPath("$.genre").value(book.getGenre()))
                .andExpect(jsonPath("$.authors", containsInAnyOrder(book.getAuthors().toArray())));
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void updateBook_withInvalidTitle_shouldReturnBadRequest() throws Exception {
        String invalidTitle = " ";

        String requestBody = String.format("""
        {
            "title": "%s"
        }
        """, invalidTitle);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/book/" + book.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Field must not be null or blank if present"));
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void updateBook_withoutBody_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/book/" + book.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is missing or malformed"));
        assertCountEquals(bookRepository, 1);
    }

    @Test
    void updateBook_withoutToken_shouldReturnUnauthorized() throws Exception {
        String updatedTitle = "Updated Title";
        String requestBody = String.format("""
        {
            "title": "%s"
        }
        """, updatedTitle);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
        assertCountEquals(bookRepository, 1);
    }

}