package gblas.books.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.TrueOrFalse.TrueOrFalseAnswerRequest;
import gblas.books.backend.entity.*;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.question.TrueOrFalseQuestionEntity;
import gblas.books.backend.mapper.answer.AnswerMapper;
import gblas.books.backend.mapper.answer.AnswerMapperFactory;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuizAttemptControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private ChapterRepository chapterRepository;
    @Autowired private QuizRepository quizRepository;
    @Autowired private QuizVersionRepository quizVersionRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private QuizAttemptRepository quizAttemptRepository;
    @Autowired private AnswerRepository answerRepository;
    @Autowired private AnswerMapperFactory answerMapperFactory;
    @Autowired private JwtService jwtService;

    private String authToken;
    private UserEntity user;
    private BookEntity book;
    private ChapterEntity chapter;
    private ChapterEntity anotherChapter;
    private QuizEntity quiz;
    private TrueOrFalseQuestionEntity question;
    private QuizAttemptEntity attempt;
    private AnswerEntity answer;

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

        attempt = new QuizAttemptEntity();
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setQuizVersion(firstVersion);
        quizAttemptRepository.save(attempt);

        List<AnswerEntity> answers = new ArrayList<>();
        AnswerRequest request = new TrueOrFalseAnswerRequest(QuestionEntity.QuestionType.TRUE_FALSE, question.getId(), true);
        answer = AnswerMapper.INSTANCE.toEntity(request, attempt, question, answerMapperFactory);
        answers.add(answer);
        answer = answerRepository.save(answer);

        attempt.setAnswers(answers);
        attempt.setCorrectCountFromAnswers();

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
        quizAttemptRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    void getQuizAttemptsFromUser_withAuthorizedUser_shouldReturnPage() throws Exception {
        mockMvc.perform(get("/api/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].quiz_id").value(quiz.getId().toString()))
                .andExpect(jsonPath("$.content[0].answers[0].question.id").value(question.getId().toString()))
                .andExpect(jsonPath("$.content[0].answers[0].isCorrect").value(true))
                .andExpect(jsonPath("$.content[0].answers[0].answer").value(true));
    }

    @Test
    void getQuizAttemptsFromUser_withUnauthorizedUser_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/quiz-attempts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getQuizAttemptsFromBook_withValidBook_shouldReturnPage() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].quiz_id").value(quiz.getId().toString()))
                .andExpect(jsonPath("$.content[0].answers[0].question.id").value(question.getId().toString()))
                .andExpect(jsonPath("$.content[0].answers[0].isCorrect").value(true))
                .andExpect(jsonPath("$.content[0].answers[0].answer").value(true));
    }

    @Test
    void getQuizAttemptsFromBook_withInvalidBookId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/books/invalid-uuid/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getQuizAttemptsFromBook_withNonexistentBook_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/books/" + UUID.randomUUID() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getQuizAttemptFromChapter_withValidChapter_shouldReturnPage() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/chapters/" + chapter.getId() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].quiz_id").value(quiz.getId().toString()))
                .andExpect(jsonPath("$.content[0].answers[0].question.id").value(question.getId().toString()))
                .andExpect(jsonPath("$.content[0].answers[0].isCorrect").value(true))
                .andExpect(jsonPath("$.content[0].answers[0].answer").value(true));
    }

    @Test
    void getQuizAttemptFromChapter_withInvalidChapterId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/chapters/invalid-uuid/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getQuizAttemptFromChapter_withNonexistentChapter_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/books/" + book.getId() + "/chapters/" + UUID.randomUUID() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void createQuizAttempt_withValidData_shouldCreateAttempt() throws Exception {
        String startedAtExpected = "2025-07-31T10:20:00";
        String requestBody = """
        {
            "startedAt": "%s",
            "answers": [
                {
                    "type": "TRUE_FALSE",
                    "questionId": "%s",
                    "answer": true
                }
            ]
        }
        """.formatted(startedAtExpected, question.getId());

        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quiz_id").value(quiz.getId().toString()))
                .andExpect(jsonPath("$.answers[0].question.id").value(question.getId().toString()))
                .andExpect(jsonPath("$.answers[0].isCorrect").value(true))
                .andExpect(jsonPath("$.answers[0].answer").value(true))
                .andExpect(jsonPath("$.correctCount").value(1))
                .andExpect(jsonPath("$.startedAt").value(startedAtExpected))
                .andExpect(jsonPath("$.submittedAt").isNotEmpty());
    }

    @Test
    void createQuizAttempt_withNonexistentQuiz_shouldReturnNotFound() throws Exception {
        String requestBody = """
        {
            "startedAt": "2025-07-31T10:20:00",
            "answers": [
            {
                "type": "TRUE_FALSE",
                "questionId": "%s",
                "answer": true
            }
            ]
        }
        """.formatted(question.getId());

        mockMvc.perform(post("/api/quizzes/" + UUID.randomUUID() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void createQuizAttempt_withInvalidQuestionId_shouldReturnBadRequest() throws Exception {
        String requestBody = """
        {
            "answers": [
            {
                "type": "TRUE_FALSE",
                "questionId": "invalid-uuid",
                "isAnswerTrue": true
            }
            ]
        }
        """;

        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createQuizAttempt_missingType_shouldReturnBadRequest() throws Exception {
        String requestBody = """
        {
            "startedAt": "2025-07-31T10:20:00",
            "answers": [
                {
                    "questionId": "%s",
                    "answer": true
                }
            ]
        }
        """.formatted(question.getId());

        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is missing or malformed"));
    }

    @Test
    void createQuizAttempt_missingQuestionId_shouldReturnBadRequest() throws Exception {
        String requestBody = """
        {
            "startedAt": "2025-07-31T10:20:00",
            "answers": [
                {
                    "type": "TRUE_FALSE",
                    "answer": true
                }
            ]
        }
        """;

        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.['answers[0].questionId']").value("must not be null"));
    }

    @Test
    void createQuizAttempt_missingIsAnswerTrue_shouldReturnBadRequest() throws Exception {
        String requestBody = """
        {
            "startedAt": "2025-07-31T10:20:00",
            "answers": [
                {
                    "type": "TRUE_FALSE",
                    "questionId": "%s"
                }
            ]
        }
        """.formatted(question.getId());

        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.['answers[0].answer']").value("must not be null"));
    }

    @Test
    void createQuizAttempt_missingAnswers_shouldReturnBadRequest() throws Exception {
        String requestBody = """
        {
            "startedAt": "2025-07-31T10:20:00"
        }
        """;

        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }


    @Test
    void createQuizAttempt_withInvalidStartedAt_shouldReturnBadRequest() throws Exception {
        String invalidStartedAt = LocalDateTime.now().plusYears(1).toString();
        String requestBody = """
        {
            "startedAt": "%s",
            "answers": [
            {
                "type": "TRUE_FALSE",
                "questionId": "%s",
                "isAnswerTrue": true
            }
            ]
        }
        """.formatted(invalidStartedAt, question.getId());

        mockMvc.perform(post("/api/quizzes/" + quiz.getId() + "/quiz-attempts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("startedAt").value("must be a date in the past or in the present"));
    }

    @Test
    void deleteQuizAttempt_withValidId_shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/quiz-attempts/" + attempt.getId() + "/")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteQuizAttempt_withInvalidId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/quiz-attempts/invalid-uuid/")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteQuizAttempt_withNonexistentId_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/quiz-attempts/" + UUID.randomUUID() + "/")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }
}