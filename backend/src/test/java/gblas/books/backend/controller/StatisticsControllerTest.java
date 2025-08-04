package gblas.books.backend.controller;

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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StatisticsControllerTest {

    @Autowired private MockMvc mockMvc;
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
    private BookEntity book;
    private ChapterEntity chapter;

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

        chapter = new ChapterEntity();
        chapter.setNumber(1);
        chapter.setTitle("Chapter 1");
        chapter.setSummary("Content 1");
        chapter.setBook(book);
        chapter = chapterRepository.save(chapter);

        ChapterEntity anotherChapter = new ChapterEntity();
        anotherChapter.setNumber(2);
        anotherChapter.setTitle("Chapter 2");
        anotherChapter.setSummary("Content 2");
        anotherChapter.setBook(book);
        anotherChapter = chapterRepository.save(anotherChapter);

        QuizEntity quiz = new QuizEntity();
        chapter.setQuizBidirectional(quiz);
        quiz = quizRepository.save(quiz);

        QuizVersionEntity firstVersion = new QuizVersionEntity();
        firstVersion.setQuiz(quiz);
        firstVersion.setIsCurrent(true);
        firstVersion = quizVersionRepository.save(firstVersion);

        TrueOrFalseQuestionEntity question = new TrueOrFalseQuestionEntity();
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

        QuizAttemptEntity attempt = new QuizAttemptEntity();
        LocalDateTime time = LocalDateTime.now();
        attempt.setStartedAt(time);
        attempt.setSubmittedAt(time.plusSeconds(60));
        attempt.setQuizVersion(firstVersion);
        quizAttemptRepository.save(attempt);

        List<AnswerEntity> answers = new ArrayList<>();
        AnswerRequest request = new TrueOrFalseAnswerRequest(QuestionEntity.QuestionType.TRUE_FALSE, question.getId(), true);
        AnswerEntity answer = AnswerMapper.INSTANCE.toEntity(request, attempt, question, answerMapperFactory);
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
    void getStatisticsFromUser_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/statistics/user")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats.totalAttempts").value(1))
                .andExpect(jsonPath("$.stats.totalQuizzesAttempted").value(1))
                .andExpect(jsonPath("$.stats.averageTimeSeconds").value(60))
                .andExpect(jsonPath("$.stats.averageScore").value(1))
                .andExpect(jsonPath("$.stats.bestScore").value(1))
                .andExpect(jsonPath("$.stats.worstScore").value(1))
                .andExpect(jsonPath("$.books[0].bookId").value(book.getId().toString()));
    }

    @Test
    void getStatisticsFromUser_noAttempts_shouldReturnNoContent() throws Exception {
        quizAttemptRepository.deleteAll();
        mockMvc.perform(get("/api/statistics/user")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void getStatisticsFromBook_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/statistics/books/" + book.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats.totalAttempts").value(1))
                .andExpect(jsonPath("$.stats.totalQuizzesAttempted").value(1))
                .andExpect(jsonPath("$.stats.averageTimeSeconds").value(60))
                .andExpect(jsonPath("$.stats.averageScore").value(1))
                .andExpect(jsonPath("$.stats.bestScore").value(1))
                .andExpect(jsonPath("$.stats.worstScore").value(1))
                .andExpect(jsonPath("$.chapters[0].chapterId").value(chapter.getId().toString()));
    }

    @Test
    void getStatisticsFromBook_noAttempts_shouldReturnNoContent() throws Exception {
        quizAttemptRepository.deleteAll();
        mockMvc.perform(get("/api/statistics/books/" + book.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void getStatisticsFromBook_notFound_shouldReturnNotFound() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/api/statistics/books/" + fakeId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void getStatisticsFromChapter_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/statistics/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats.totalAttempts").value(1))
                .andExpect(jsonPath("$.stats.totalQuizzesAttempted").value(1))
                .andExpect(jsonPath("$.stats.averageTimeSeconds").value(60))
                .andExpect(jsonPath("$.stats.averageScore").value(1))
                .andExpect(jsonPath("$.stats.bestScore").value(1))
                .andExpect(jsonPath("$.stats.worstScore").value(1))
                .andExpect(jsonPath("$.chapterId").value(chapter.getId().toString()));
    }

    @Test
    void getStatisticsFromChapter_noAttempts_shouldReturnNoContent() throws Exception {
        quizAttemptRepository.deleteAll();
        mockMvc.perform(get("/api/statistics/chapters/" + chapter.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void getStatisticsFromChapter_notFound_shouldReturnNotFound() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/api/statistics/chapters/" + fakeId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Chapter not found"));
    }

}