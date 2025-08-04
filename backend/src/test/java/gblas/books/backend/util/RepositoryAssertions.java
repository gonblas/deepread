package gblas.books.backend.util;

import org.springframework.data.repository.CrudRepository;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepositoryAssertions {

    public static void assertCountEquals(CrudRepository<?, ?> repository, int expectedCount) {
        long actualCount = StreamSupport.stream(repository.findAll().spliterator(), false).count();
        assertEquals(expectedCount, actualCount,
                "Expected " + expectedCount + " entities, but found " + actualCount);
    }

    public static void assertIsEmpty(CrudRepository<?, ?> repository) {
        assertCountEquals(repository, 0);
    }

    public static void assertIsNotEmpty(CrudRepository<?, ?> repository) {
        long actualCount = StreamSupport.stream(repository.findAll().spliterator(), false).count();
        if (actualCount == 0) {
            throw new AssertionError("Expected repository to have at least one entity, but it was empty.");
        }
    }
}
