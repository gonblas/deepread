package gblas.books.backend.controller;

import gblas.books.backend.dto.*;
import gblas.books.backend.dto.question.MultipleChoice.MultipleChoiceQuestionRequest;
import gblas.books.backend.dto.question.Open.OpenQuestionRequest;
import gblas.books.backend.dto.question.TrueOrFalse.TrueOrFalseQuestionRequest;
import gblas.books.backend.entity.*;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.repository.*;
import gblas.books.backend.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static gblas.books.backend.util.RepositoryAssertions.assertCountEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private ChapterRepository chapterRepository;
    @Autowired private QuizRepository quizRepository;
    @Autowired private QuizVersionRepository quizVersionRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private OptionRepository optionRepository;
    @Autowired private ChapterService chapterService;
    @Autowired private BookService bookService;
    @Autowired private AuthService authService;
    @Autowired private QuizService quizService;
    @Autowired private JwtService jwtService;

    private String authToken;
    private UUID quizId;
    private UUID trueOrFalseQuestionId;
    private UUID openQuestionId;
    private UUID multipleChoiceQuestionId;

    @BeforeEach
    void setUp() {
        RegisterRequest registerRequest = new RegisterRequest("test@example.com", "encoded_password", "testuser");
        authService.register(registerRequest);
        UserEntity newUser = userRepository.findByEmail("test@example.com").orElseThrow(() -> new UsernameNotFoundException("testuser"));

                BookRequest bookRequest = new BookRequest("title", "description", BookEntity.BookGenre.ECONOMICS, List.of("Lionel Messi"));
        UUID bookId = bookService.addBook(newUser, bookRequest).id();

        ChapterRequest chapterRequest = new ChapterRequest("title", 1, "summary");
        UUID chapterId = chapterService.addChapter(bookId, chapterRequest).id();

        QuizRequest quizRequest = getQuizRequest();
        QuizResponse quizResponse = quizService.addQuiz(bookId, chapterId, quizRequest);
        quizId = quizResponse.id();
        trueOrFalseQuestionId = quizResponse.questions().getFirst().id();
        openQuestionId = quizResponse.questions().get(1).id();
        multipleChoiceQuestionId  = quizResponse.questions().get(2).id();

        authToken = jwtService.generateToken(newUser.getEmail());
    }

    private static QuizRequest getQuizRequest() {
        TrueOrFalseQuestionRequest trueOrFalseRequest = new TrueOrFalseQuestionRequest(QuestionEntity.QuestionType.TRUE_FALSE, "prompt", "explanation", true);
        OpenQuestionRequest openRequest = new OpenQuestionRequest(QuestionEntity.QuestionType.OPEN, "prompt", "explanation", "expectedAnswer");
        OptionRequest optionRequest = new OptionRequest("optionText", true);
        MultipleChoiceQuestionRequest multipleChoiceRequest = new MultipleChoiceQuestionRequest(QuestionEntity.QuestionType.MULTIPLE_CHOICE, "prompt", "explanation", List.of(optionRequest));

        return new QuizRequest(List.of(trueOrFalseRequest, openRequest, multipleChoiceRequest));
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
    void createTrueOrFalseQuestion_shouldReturnCreated() throws Exception {
        String requestBody = """
                {
                    "type": "TRUE_FALSE",
                    "prompt": "What is Java?",
                    "explanation": "Basic knowledge",
                    "isAnswerTrue": true
                }
                """;

        mockMvc.perform(post("/api/" + quizId + "/questions")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prompt").value("What is Java?"))
                .andExpect(jsonPath("$.explanation").value("Basic knowledge"))
        .andExpect(jsonPath("$.isAnswerTrue").value(true));
        assertCountEquals(questionRepository, 4);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isPresent());
    }

    @Test
    void createOpenQuestion_shouldReturnCreated() throws Exception {
        String requestBody = """
                {
                    "type": "OPEN",
                    "prompt": "What is Java?",
                    "explanation": "Basic knowledge",
                    "expectedAnswer": "A programming language"
                }
                """;

        mockMvc.perform(post("/api/" + quizId + "/questions")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prompt").value("What is Java?"))
                .andExpect(jsonPath("$.explanation").value("Basic knowledge"))
                .andExpect(jsonPath("$.expectedAnswer").value("A programming language"));
        assertCountEquals(questionRepository, 4);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isPresent());
    }

    @Test
    void createMultipleChoiceQuestion_shouldReturnCreated() throws Exception {
        String requestBody = """
                {
                    "type": "MULTIPLE_CHOICE",
                    "prompt": "What is Java?",
                    "explanation": "Basic knowledge",
                    "options": [
                        {
                            "text": "A framework",
                            "isCorrect": false
                        },
                        {
                            "text": "A programming language",
                            "isCorrect": true
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/api/" + quizId + "/questions")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prompt").value("What is Java?"))
                .andExpect(jsonPath("$.explanation").value("Basic knowledge"))
                .andExpect(jsonPath("$.options.size()").value(2));
        assertCountEquals(questionRepository, 4);
        assertCountEquals(optionRepository, 3);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isPresent());
    }

    @Test
    void createQuestion_withInvalidQuiz_shouldReturnNotFound() throws Exception {
        String requestBody = """
                {
                    "type": "TRUE_FALSE",
                    "prompt": "Java is statically typed?",
                    "explanation": "It helps the compiler.",
                    "isAnswerTrue": true
                }
                """;

        mockMvc.perform(post("/api/" + UUID.randomUUID() + "/questions")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
        assertCountEquals(questionRepository, 3);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isPresent());
    }

    @Test
    void deleteTrueOrFalseQuestion_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/" + quizId + "/questions/" + trueOrFalseQuestionId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
        assertCountEquals(questionRepository, 3);
        assertCountEquals(quizRepository, 1);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isEmpty());
    }

    @Test
    void deleteOpenQuestion_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/" + quizId + "/questions/" + openQuestionId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
        assertCountEquals(questionRepository, 3);
        assertCountEquals(quizRepository, 1);
        assertTrue(questionRepository.findByIdAndCurrentVersion(openQuestionId).isEmpty());
    }

    @Test
    void deleteMultipleChoiceQuestion_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/" + quizId + "/questions/" + multipleChoiceQuestionId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
        assertCountEquals(questionRepository, 3);
        assertCountEquals(quizRepository, 1);
        assertTrue(questionRepository.findByIdAndCurrentVersion(multipleChoiceQuestionId).isEmpty());
    }

    @Test
    void deleteQuestion_withInvalidId_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/" + quizId + "/questions/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
        assertCountEquals(questionRepository, 3);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isPresent());
    }

    @Test
    void deleteQuestion_withInvalidQuiz_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/" + UUID.randomUUID() + "/questions/" + trueOrFalseQuestionId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
        assertCountEquals(questionRepository, 3);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isPresent());
    }

    @Test
    void updateQuestion_shouldReturnUpdated() throws Exception {
        String updatedJson = """
                {
                    "type": "TRUE_FALSE",
                    "prompt": "Updated question?",
                    "explanation": "Updated explanation",
                    "isAnswerTrue": false
                }
                """;

        mockMvc.perform(put("/api/" + quizId + "/questions/" + trueOrFalseQuestionId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prompt").value("Updated question?"));
        assertCountEquals(questionRepository, 4);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isEmpty());
    }

    @Test
    void updateQuestion_withInvalidQuiz_shouldReturnNotFound() throws Exception {
        String body = """
                {
                    "type": "OPEN",
                    "prompt": "New?",
                    "expectedAnswer": "Yeah"
                }
                """;

        mockMvc.perform(put("/api/" + UUID.randomUUID() + "/questions/" + trueOrFalseQuestionId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
        assertCountEquals(questionRepository, 3);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isPresent());
    }

    @Test
    void updateQuestion_withInvalidQuestion_shouldReturnNotFound() throws Exception {
        String body = """
                {
                    "type": "OPEN",
                    "prompt": "New?",
                    "expectedAnswer": "Yeah"
                }
                """;

        mockMvc.perform(put("/api/" + quizId + "/questions/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
        assertCountEquals(questionRepository, 3);
        assertTrue(questionRepository.findByIdAndCurrentVersion(trueOrFalseQuestionId).isPresent());
    }
}