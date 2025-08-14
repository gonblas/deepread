# Learning books Backend

This is the backend API for the Books-App learning platform. It provides endpoints to manage users, books, chapters, quizzes, and quiz attempts.

## Application Goal

The application is a learning platform that allows users to manage books and their content. Users can create books, divide them into chapters, make summaries and associate a quiz with each chapter. Quizzes are composed of different types of questions (such as True/False and Open-ended) to assess knowledge. The system also handles user authentication qith JWT and tracks quiz attempts. 

## Getting Started

Follow these instructions to get the project up and running on your local machine for development and testing purposes.

### Prerequisites

-   [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) or later
-   [Apache Maven](https://maven.apache.org/download.cgi)
-   [Docker](https://www.docker.com/products/docker-desktop/) and [Docker Compose](https://docs.docker.com/compose/install/)

### Configuration

The application configuration is located in `src/main/resources/application.properties`. You may need to create a local override or set environment variables for the following properties:

-   **Database**:
    -   `spring.datasource.url`: The JDBC URL of your database.
    -   `spring.datasource.username`: The database user.
    -   `spring.datasource.password`: The database user's password.
-   **JWT Secret**:
    -   `application.security.jwt.secret-key`: A strong secret key for signing JWTs.

### Running Locally

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/gonblas/books-app
    cd books-app
    ```

2.  **Build the project:** This will download dependencies and compile the source code.
    ```bash
    docker-compose up db backend --build
    ```

The application will start on `http://localhost:8080`.


## API Documentation (Swagger)

The API is documented using OpenAPI 3. Once the application is running, you can access the Swagger UI to explore and test the endpoints at the following URL:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Project Structure

The project follows a layered architecture, separating responsibilities into different packages:

-   `config`: Contains Spring configuration classes.
-   `controller`: Handles incoming HTTP requests. Defines the REST API endpoints for each resource.
-   `dto`: Contains Data Transfer Objects (DTOs). Used to model the API request and response bodies, decoupling the API layer from the database entities.
-   `entity`: Contains the JPA entities that are mapped to database tables.
-   `exceptions`: Defines custom exception classes for handling application-specific errors.
-   `mapper`: MapStruct interfaces for converting DTOs to Entities and vice-versa, reducing boilerplate code.
-   `repository`: Spring Data JPA interfaces for data access operations.
-   `service`: Contains the core business logic of the application.

## Testing

Unit and integration tests are located in the `src/test/java/gblas/books/backend` directory. The project uses JUnit 5 along with Spring Boot Test to create integration tests that load the application context.

To run the tests, execute the following Maven command:
```bash
mvn test
```

## Design Patterns

### Strategy Pattern for Polymorphic Mapping

A key challenge in the application is handling different question types (e.g., `OPEN`, `TRUE_FALSE`), each with its own data and validation logic. To manage this in a clean and extensible way, the **Strategy design pattern** is used.

**The Problem**: We need to map a `QuestionRequest` (DTO) to a `QuestionEntity` and vice-versa. Since there are multiple question types, a solution with `if-else` or `switch` statements in the mapper would be hard to maintain and extend.

**The Solution (Strategy)**:
-   **Strategy Interfaces**: Interfaces like `TypedQuestionMapper` and `TypedAnswerMapper` are defined. Each establishes a common contract for mappers of a specific type.
-   **Concrete Strategies**: Classes like `OpenQuestionMapper` and `TrueOrFalseQuestionMapper` implement the strategy interface, providing the specific mapping logic for their question type.
-   **Context (Factory)**: The `QuestionMapperFactory` class acts as the context. Spring injects a list of all available strategies (mappers). The factory let us select and return the correct strategy at runtime based on the question type.
-   **Usage**: Generic mappers like `QuestionMapper` use the factory to delegate the mapping task to the appropriate strategy.

This approach makes the system highly extensible. To add a new question type, one only needs to create its DTO and Entity classes and a new `TypedQuestionMapper` implementation, without modifying any existing mapping code.

#### How to Add a New Question

1. **Add the entity** for the new question type.
2. **Add the enum type** for this entity. If needed, also create any additional helper entities.
3. **Add the DTOs** for both question and answer. If new entities were added in step 2, create any extra DTOs required for them.


### Quiz Versioning for Historical Integrity

To ensure that user quiz attempts remain accurate over time, the application implements a transparent versioning system for quizzes.

**The Problem**: A user takes a quiz. Later, the author of the quiz might modify, add, or delete questions. When reviewing a past attempt, the user must see the exact questions and answers as they were when the quiz was taken, not the current version of the quiz.

**The Solution (Versioning)**:
-   Whenever a quiz is created or updated, the system creates a new, immutable `QuizVersion`. This version is a snapshot of the quiz and all its questions at that specific point in time.
-   When a user starts a quiz, their `QuizAttempt` is linked to the latest `QuizVersion`, not the live `Quiz` entity.
-   This ensures that every attempt is permanently associated with the exact state of the quiz at the moment it was taken.

This mechanism is transparent to the end-user but guarantees the historical integrity of all quiz attempts, even if the original quiz is changed.