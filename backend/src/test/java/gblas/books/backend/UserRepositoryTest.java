package gblas.books.backend;

import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        testUser = new UserEntity();
        testUser.setEmail("example@xyz.com");
        testUser.setUsername("example");
        testUser.setHashedPassword("this_is_an_example");
        userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        userRepository.delete(testUser);
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundById() {
        UserEntity savedUser = userRepository.findByEmail(testUser.getEmail()).orElse(null);
        assertNotNull(savedUser);
    }
}