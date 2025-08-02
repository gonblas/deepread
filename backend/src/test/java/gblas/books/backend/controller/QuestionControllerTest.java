package gblas.books.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gblas.books.backend.dto.question.TrueOrFalse.TrueOrFalseQuestionRequest;
import gblas.books.backend.entity.*;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.question.TrueOrFalseQuestionEntity;
import gblas.books.backend.mapper.question.QuestionMapper;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
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

import java.util.List;
import java.util.UUID;

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
    @Autowired private QuestionMapperFactory questionMapperFactory;
    @Autowired private JwtService jwtService;

    private String authToken;
    private QuizEntity quiz;
    private QuestionEntity question;
    private ChapterEntity chapter;
    private QuizVersionEntity quizVersion;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setHashedPassword("encoded_password");
        user = userRepository.save(user);

        BookEntity book = new BookEntity();
        book.setTitle("Test Book");
        book.setOwner(user);
        book = bookRepository.save(book);

        chapter = new ChapterEntity();
        chapter.setNumber(1);
        chapter.setTitle("Chapter 1");
        chapter.setSummary("Summary 1");
        chapter.setBook(book);
        chapter = chapterRepository.save(chapter);

        quiz = new QuizEntity();
        chapter.setQuizBidirectional(quiz);
        quizRepository.save(quiz);

        quizVersion = new QuizVersionEntity();

        quizVersion.setQuiz(quiz);
        quizVersion.setIsCurrent(true);
        quizVersionRepository.save(quizVersion);

        question = QuestionMapper.INSTANCE.toEntity(new TrueOrFalseQuestionRequest(QuestionEntity.QuestionType.TRUE_FALSE, "prompt", "explanation", true), questionMapperFactory);

        question.setQuiz(quiz);
        question = questionRepository.save(question);
        question.getVersions().add(quizVersion);

        quizVersion.getQuestions().add(question);
        quiz.getQuestions().add(question);
        quiz.getVersions().add(quizVersion);

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
    void createTrueOrFalseQuestion_shouldReturnCreated() throws Exception {
        String requestBody = """
                {
                    "type": "TRUE_FALSE",
                    "prompt": "Java is statically typed?",
                    "explanation": "It helps the compiler.",
                    "isAnswerTrue": true
                }
                """;

        mockMvc.perform(post("/api/" + quiz.getId() + "/questions")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prompt").value("Java is statically typed?"));
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

        mockMvc.perform(post("/api/" + quiz.getId() + "/questions")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prompt").value("What is Java?"));
    }

//    @Test
//    void deleteQuestion_shouldReturnNoContent() throws Exception {
//        mockMvc.perform(delete("/api/" + quiz.getId() + "/questions/" + question.getId())
//                        .header("Authorization", "Bearer " + authToken))
//                .andExpect(status().isNoContent());
//    }

    @Test
    void deleteQuestion_withInvalidId_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/" + quiz.getId() + "/questions/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteQuestion_withInvalidQuiz_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/" + UUID.randomUUID() + "/questions/" + question.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
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

        mockMvc.perform(put("/api/" + quiz.getId() + "/questions/" + question.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prompt").value("Updated question?"));
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

        mockMvc.perform(put("/api/" + UUID.randomUUID() + "/questions/" + question.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
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

        mockMvc.perform(put("/api/" + quiz.getId() + "/questions/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
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
    }
}