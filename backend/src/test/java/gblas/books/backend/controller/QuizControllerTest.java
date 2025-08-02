package gblas.books.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gblas.books.backend.entity.*;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.question.TrueOrFalseQuestionEntity;
import gblas.books.backend.repository.*;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private ChapterRepository chapterRepository;
    @Autowired private QuizRepository quizRepository;
    @Autowired private QuizVersionRepository quizVersionRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private JwtService jwtService;

    private String authToken;
    private UserEntity user;
    private BookEntity book;
    private ChapterEntity chapter;
    private ChapterEntity anotherChapter;
    private QuizEntity quiz;
    private TrueOrFalseQuestionEntity question;

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

        quiz = new QuizEntity();
        chapter.setQuizBidirectional(quiz);
        quiz = quizRepository.save(quiz);

        QuizVersionEntity firstVersion = new QuizVersionEntity();
        firstVersion.setQuiz(quiz);
        firstVersion.setIsCurrent(true);
        firstVersion = quizVersionRepository.save(firstVersion);

        question = new TrueOrFalseQuestionEntity();
        question.setType(QuestionEntity.QuestionType.TRUE_FALSE);
        question.setPrompt("prompt");
        question.setIsAnswerTrue(true);
        question.setQuiz(quiz);
        question.getVersions().add(firstVersion);

        firstVersion.getQuestions().add(question);
        quiz.getQuestions().add(question);
        quiz.getVersions().add(firstVersion);

        question = questionRepository.save(question);

        quizVersionRepository.save(firstVersion);
        quizRepository.save(quiz);

        authToken = jwtService.generateToken(user.getEmail());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        bookRepository.deleteAll();
        chapterRepository.deleteAll();
        quizRepository.deleteAll();
        quizVersionRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    void getQuizzesFromUser_withAuthorizedUser_shouldReturnPage() throws Exception {
        mockMvc.perform(get("/api/quizzes")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].chapter.id").value(chapter.getId().toString()))
                .andExpect(jsonPath("$.content[0].questions[0].id").value(question.getId().toString()));
    }

    @Test
    void getQuizzesFromUser_withUnauthorizedUser_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/quizzes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getQuizzesFromBook_withValidBook_shouldReturnPage() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/quizzes")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].chapter.id").value(chapter.getId().toString()));
    }

    @Test
    void getQuizzesFromBook_withNonexistentBook_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/books/" + UUID.randomUUID() + "/quizzes")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getQuizzesFromBook_withInvalidBookId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/books/invalid-uuid/quizzes")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getQuizFromChapter_withValidChapter_shouldReturnQuiz() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/chapters/" + chapter.getId() + "/quiz")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chapter.id").value(chapter.getId().toString()));
    }

    @Test
    void getQuizFromChapter_withNonexistentChapter_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/chapters/" + UUID.randomUUID() + "/quiz")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getQuizFromChapter_withInvalidChapterId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/chapters/invalid-uuid/quiz")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createQuiz_withValidData_shouldCreateQuiz() throws Exception {
        String requestBody = """
        {
            "questions": [
            {
              "type": "OPEN",
              "prompt": "prompt",
              "explanation": "explanation",
              "expectedAnswer": "expectedAnswer"
            }
            ]
        }
        """;

        mockMvc.perform(post("/api/books/" + book.getId() + "/chapters/" + anotherChapter.getId() + "/quiz")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.chapter.id").value(anotherChapter.getId().toString()));
    }

    @Test
    void createQuiz_withEmptyQuestionList_shouldReturnBadRequest() throws Exception {
        String requestBody = """
        {
            "questions": []
        }
        """;

        mockMvc.perform(post("/api/books/" + book.getId() + "/chapters/" + anotherChapter.getId() + "/quiz")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.questions").value("Questions list cannot be empty"));
    }

    @Test
    void createQuiz_withChapterAlreadyHavingQuiz_shouldReturnConflict() throws Exception {
        String requestBody = """
        {
            "questions": [
            {
              "type": "OPEN",
              "prompt": "prompt",
              "explanation": "explanation",
              "expectedAnswer": "expectedAnswer"
            }
            ]
        }
        """;

        mockMvc.perform(post("/api/books/" + book.getId() + "/chapters/" + chapter.getId() + "/quiz")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    void createQuiz_withNonexistentChapter_shouldReturnNotFound() throws Exception {
        String requestBody = """
        {
            "questions": [
            {
              "type": "OPEN",
              "prompt": "prompt",
              "explanation": "explanation",
              "expectedAnswer": "expectedAnswer"
            }
            ]
        }
        """;

        mockMvc.perform(post("/api/books/" + book.getId() + "/chapters/" + UUID.randomUUID() + "/quiz")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void createQuiz_withNonexistentBook_shouldReturnNotFound() throws Exception {
        String requestBody = """
        {
            "questions": [
            {
              "type": "OPEN",
              "prompt": "prompt",
              "explanation": "explanation",
              "expectedAnswer": "expectedAnswer"
            }
            ]
        }
        """;

        mockMvc.perform(post("/api/books/" + UUID.randomUUID() + "/chapters/" + chapter.getId() + "/quiz")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void createQuiz_withChapterNotBelongingToBook_shouldReturnNotFound() throws Exception {
        BookEntity otherBook = new BookEntity();
        otherBook.setTitle("Other");
        otherBook.setOwner(user);
        otherBook = bookRepository.save(otherBook);

        String requestBody = """
        {
            "questions": [
            {
              "type": "OPEN",
              "prompt": "prompt",
              "explanation": "explanation",
              "expectedAnswer": "expectedAnswer"
            }
            ]
        }
        """;

        mockMvc.perform(post("/api/books/" + otherBook.getId() + "/chapters/" + chapter.getId() + "/quiz")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteQuiz_withValidChapter_shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/books/" + book.getId() + "/chapters/" + chapter.getId() + "/quiz")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteQuiz_withNonexistentChapter_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/books/" + book.getId() + "/chapters/" + UUID.randomUUID() + "/quiz")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteQuiz_withNonexistentBook_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/books/" + UUID.randomUUID() + "/chapters/" + chapter.getId() + "/quiz")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteQuiz_withChapterNotBelongingToBook_shouldReturnNotFound() throws Exception {
        BookEntity otherBook = new BookEntity();
        otherBook.setTitle("Other");
        otherBook.setOwner(user);
        otherBook = bookRepository.save(otherBook);

        mockMvc.perform(delete("/api/books/" + otherBook.getId() + "/chapters/" + chapter.getId() + "/quiz")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }
}
