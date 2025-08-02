package gblas.books.backend.repository;

import gblas.books.backend.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuizRepositoryTest {
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private ChapterRepository chapterRepository;
    @Autowired private QuizRepository quizRepository;
    private Pageable pageable;
    private QuizEntity quizExpected;
    private UUID userId;
    private UUID bookId;
    private UUID chapterId;
    @Autowired private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user = new UserEntity();
        user.setEmail("example@xyz.com");
        user.setUsername("example");
        user.setHashedPassword("this_is_an_example");
        UserEntity newUser = entityManager.persist(user);
        userId = user.getId();

        BookEntity book = new BookEntity();
        book.setOwner(newUser);
        book.setTitle("title");
        bookRepository.save(book);
//
//        ChapterEntity chapter = new ChapterEntity();
//        chapter.setBook(book);
//        chapter.setTitle("title");
//        chapter.setNumber(1);
//        chapterId = chapterRepository.save(chapter).getId();
//
//        QuizEntity quiz = new QuizEntity();
//        quiz.setChapter(chapter);
//        quizExpected = quizRepository.save(quiz);

        pageable = PageRequest.of(0, 10);
    }


    @Test
    void findAllQuizzesByBookId() {
    }

    @Test
    void findByChapter() {
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        bookRepository.deleteAll();
        chapterRepository.deleteAll();
        quizRepository.deleteAll();
    }
}