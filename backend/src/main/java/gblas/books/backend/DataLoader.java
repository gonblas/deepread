package gblas.books.backend;

import gblas.books.backend.entity.*;
import gblas.books.backend.entity.answer.MultipleChoiceAnswerEntity;
import gblas.books.backend.entity.answer.OpenAnswerEntity;
import gblas.books.backend.entity.answer.TrueOrFalseAnswerEntity;
import gblas.books.backend.entity.question.MultipleChoiceQuestionEntity;
import gblas.books.backend.entity.question.OpenQuestionEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.entity.question.TrueOrFalseQuestionEntity;
import gblas.books.backend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChapterRepository chapterRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final AnswerRepository answerRepository;
    private final QuizVersionRepository quizVersionRepository;

    @Override
    public void run(ApplicationArguments args) {
        // Usuario demo
        UserEntity demoUser = createUser("demo@example.com", "DemoUser", "password");

        // ----------------- LIBROS -----------------
        BookEntity book1 = createBook(demoUser, "Clean Code", "A Handbook of Agile Software Craftsmanship",
                BookEntity.BookGenre.PROGRAMMING, "Robert C. Martin");

        BookEntity book2 = createBook(demoUser, "The Pragmatic Programmer", "Your Journey to Mastery",
                BookEntity.BookGenre.PROGRAMMING, "Andrew Hunt", "David Thomas");

        BookEntity book3 = createBook(demoUser, "Design Patterns", "Elements of Reusable Object-Oriented Software",
                BookEntity.BookGenre.PROGRAMMING, "Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides");

        BookEntity book4 = createBook(demoUser, "Effective Java", "Best practices for Java programming",
                BookEntity.BookGenre.PROGRAMMING, "Joshua Bloch");

        BookEntity book5 = createBook(demoUser, "Capital in the Twenty-First Century", "Analysis of wealth and inequality",
                BookEntity.BookGenre.ECONOMICS, "Thomas Piketty");

        BookEntity book6 = createBook(demoUser, "Freakonomics", "A Rogue Economist Explores the Hidden Side of Everything",
                BookEntity.BookGenre.ECONOMICS, "Steven D. Levitt", "Stephen J. Dubner");

        // ----------------- CAPÍTULOS + QUIZZES -----------------
        // Book 1: Clean Code
        ChapterEntity chapter1_1 = createChapter(book1, 1, "Meaningful Names",
                "How to use clear and meaningful names for variables, functions, and classes.");
        ChapterEntity chapter1_2 = createChapter(book1, 2, "Functions",
                "Writing small, readable, and well-structured functions.");
        ChapterEntity chapter1_3 = createChapter(book1, 3, "Comments",
                "When and how to use comments effectively.");

        QuizEntity quiz1 = createQuiz(chapter1_1,
                new QuestionSpec("Which of the following is NOT a characteristic of clean code?",
                        "Complicated",
                        List.of(
                                new OptionSpec("Readable", false),
                                new OptionSpec("Complicated", true),
                                new OptionSpec("Maintainable", false)
                        )),
                new QuestionSpec("Meaningful names improve code readability.", "True", true),
                new QuestionSpec("Explain why small functions are easier to maintain.",
                        "They reduce complexity and improve readability."),
                new QuestionSpec("Comments should explain why, not what the code does.", "True", true)
        );

        // Book 2: The Pragmatic Programmer
        ChapterEntity chapter2_1 = createChapter(book2, 1, "A Pragmatic Philosophy",
                "Introduction to the pragmatic programming philosophy.");
        ChapterEntity chapter2_2 = createChapter(book2, 2, "A Pragmatic Approach",
                "How to approach complex problems pragmatically.");
        ChapterEntity chapter2_3 = createChapter(book2, 3, "The Basic Tools",
                "Essential tools for a pragmatic programmer.");

        QuizEntity quiz2 = createQuiz(chapter2_1,
                new QuestionSpec("What is the primary focus of pragmatic programming?",
                        "Practical and adaptable solutions",
                        List.of(
                                new OptionSpec("Practical and adaptable solutions", true),
                                new OptionSpec("Avoiding all changes", false),
                                new OptionSpec("Strict adherence to rules", false)
                        )),
                new QuestionSpec("Pragmatic programmers value learning from mistakes.", "True", true),
                new QuestionSpec("Name one tool that is essential for a pragmatic programmer.",
                        "Version control or automated testing tools"),
                new QuestionSpec("Automation is encouraged to reduce repetitive tasks.", "True", true),
                new QuestionSpec("Explain the principle of DRY (Don't Repeat Yourself).",
                        "Avoid code duplication to improve maintainability.")
        );

        // Book 3: Design Patterns
        ChapterEntity chapter3_1 = createChapter(book3, 1, "Creational Patterns",
                "Object creation patterns such as Singleton and Factory.");
        ChapterEntity chapter3_2 = createChapter(book3, 2, "Structural Patterns",
                "Patterns that organize classes and objects, such as Adapter and Composite.");
        ChapterEntity chapter3_3 = createChapter(book3, 3, "Behavioral Patterns",
                "Patterns that define communication between objects, such as Observer and Strategy.");

        QuizEntity quiz3 = createQuiz(chapter3_1,
                new QuestionSpec("Which design pattern provides a way to create objects without specifying their concrete classes?",
                        "Factory",
                        List.of(
                                new OptionSpec("Observer", false),
                                new OptionSpec("Factory", true),
                                new OptionSpec("Singleton", false)
                        )),
                new QuestionSpec("The Singleton pattern ensures only one instance of a class exists.", "True", true),
                new QuestionSpec("Explain the advantage of using the Strategy pattern.",
                        "It allows selecting an algorithm at runtime."),
                new QuestionSpec("The Observer pattern defines a one-to-many dependency.", "True", true)
        );

        // Book 4: Effective Java
        ChapterEntity chapter4_1 = createChapter(book4, 1, "Creating and Destroying Objects",
                "Best practices for creating and destroying objects in Java.");
        ChapterEntity chapter4_2 = createChapter(book4, 2, "Methods Common to All Objects",
                "Proper use of methods like equals, hashCode, and toString.");
        ChapterEntity chapter4_3 = createChapter(book4, 3, "Generics",
                "How to use generics correctly for safety and clarity.");

        QuizEntity quiz4 = createQuiz(chapter4_1,
                new QuestionSpec("Which of the following is recommended in Effective Java for object creation?",
                        "Use static factory methods",
                        List.of(
                                new OptionSpec("Use public constructors only", false),
                                new OptionSpec("Use static factory methods", true),
                                new OptionSpec("Avoid using any design patterns", false)
                        )),
                new QuestionSpec("Immutable objects are recommended when possible.", "True", true),
                new QuestionSpec("Explain why overriding equals() and hashCode() consistently is important.",
                        "It ensures correct behavior in collections."),
                new QuestionSpec("Generics provide type safety at compile time.", "True", true),
                new QuestionSpec("What method should almost every class override?", "toString")
        );

        // Book 5: Capital in the Twenty-First Century
        ChapterEntity chapter5_1 = createChapter(book5, 1, "Income and Capital",
                "Introduction to the relationship between income and capital.");
        ChapterEntity chapter5_2 = createChapter(book5, 2, "Growth and Inequality",
                "How economic growth affects inequality.");
        ChapterEntity chapter5_3 = createChapter(book5, 3, "Capital in History",
                "The history of capital and its impact on the modern economy.");

        QuizEntity quiz5 = createQuiz(chapter5_1,
                new QuestionSpec("What is the main subject of Capital in the Twenty-First Century?",
                        "Wealth inequality",
                        List.of(
                                new OptionSpec("Climate change", false),
                                new OptionSpec("Wealth inequality", true),
                                new OptionSpec("Quantum physics", false)
                        )),
                new QuestionSpec("The book shows that wealth concentration can grow faster than economic growth.", "True", true),
                new QuestionSpec("Name one historical example of extreme wealth concentration mentioned in the book.",
                        "19th-century Europe or the US Gilded Age"),
                new QuestionSpec("Capital returns historically outpace economic growth.", "True", true)
        );

        // Book 6: Freakonomics
        ChapterEntity chapter6_1 = createChapter(book6, 1,
                "What Do Schoolteachers and Sumo Wrestlers Have in Common?",
                "Exploration of incentives and human motivations.");
        ChapterEntity chapter6_2 = createChapter(book6, 2,
                "How is the Ku Klux Klan Like a Group of Real-Estate Agents?",
                "Analysis of asymmetric information and its societal impact.");
        ChapterEntity chapter6_3 = createChapter(book6, 3,
                "Why Do Drug Dealers Still Live with Their Moms?",
                "Investigation of informal economy and human behavior.");

        QuizEntity quiz6 = createQuiz(chapter6_1,
                new QuestionSpec("Which approach does Freakonomics use to analyze everyday phenomena?",
                        "Economic analysis of incentives",
                        List.of(
                                new OptionSpec("Philosophical analysis", false),
                                new OptionSpec("Economic analysis of incentives", true),
                                new OptionSpec("Random guessing", false)
                        )),
                new QuestionSpec("Freakonomics explores hidden motives behind human behavior.", "True", true),
                new QuestionSpec("Give one example of an unusual economic phenomenon discussed in the book.",
                        "Cheating teachers or crime rates related to abortion legalization"),
                new QuestionSpec("The book argues that incentives explain a lot of human behavior.", "True", true),
                new QuestionSpec("Information asymmetry gives power to one side in a transaction.", "True", true),
                new QuestionSpec("Explain why drug dealers may continue living with their mothers.",
                        "Because their income is unstable and often low despite appearances.")
        );

        // ----------------- SIMULACIÓN DE INTENTOS -----------------
        for (QuizEntity quiz : List.of(quiz1, quiz2, quiz3, quiz4, quiz5, quiz6)) {
            simulateQuizAttempts(quiz, 100);
        }
    }



    // ---------------- Helpers ----------------

    private UserEntity createUser(String email, String username, String rawPassword) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setUsername(username);
        user.setHashedPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    private BookEntity createBook(UserEntity owner, String title, String description,
                                  BookEntity.BookGenre genre, String... authors) {
        BookEntity book = new BookEntity();
        book.setOwner(owner);
        book.setTitle(title);
        book.setDescription(description);
        book.setGenre(genre);
        book.getAuthors().addAll(List.of(authors));
        return bookRepository.save(book);
    }

    private ChapterEntity createChapter(BookEntity book, int number, String title, String summary) {
        ChapterEntity chapter = new ChapterEntity();
        chapter.setBook(book);
        chapter.setNumber(number);
        chapter.setTitle(title);
        chapter.setSummary(summary);
        return chapterRepository.save(chapter);
    }

    private QuizEntity createQuiz(ChapterEntity chapter, QuestionSpec... questions) {
        QuizEntity quiz = new QuizEntity();
        quiz.setChapter(chapter);
        quizRepository.save(quiz);

        QuizVersionEntity version = createQuizVersion(quiz);

        for (QuestionSpec spec : questions) {
            QuestionEntity q = switch (spec.type) {
                case MCQ -> buildMCQ(quiz, spec);
                case TF -> buildTF(quiz, spec);
                case OPEN -> buildOpen(quiz, spec);
            };
            version.getQuestions().add(q);
            quiz.getQuestions().add(q);
        }
        quizRepository.save(quiz);
        return quiz;
    }

    private MultipleChoiceQuestionEntity buildMCQ(QuizEntity quiz, QuestionSpec spec) {
        MultipleChoiceQuestionEntity q = new MultipleChoiceQuestionEntity();
        q.setQuiz(quiz);
        q.setPrompt(spec.prompt);
        q.setExplanation(spec.explanation);
        for (OptionSpec o : spec.options) {
            OptionEntity opt = new OptionEntity();
            opt.setText(o.text);
            opt.setIsCorrect(o.correct);
            opt.setQuestion(q);
            q.getOptions().add(opt);
        }
        return questionRepository.save(q);
    }

    private TrueOrFalseQuestionEntity buildTF(QuizEntity quiz, QuestionSpec spec) {
        TrueOrFalseQuestionEntity q = new TrueOrFalseQuestionEntity();
        q.setQuiz(quiz);
        q.setPrompt(spec.prompt);
        q.setExplanation(spec.explanation);
        q.setIsAnswerTrue(spec.correctTF);
        return questionRepository.save(q);
    }

    private OpenQuestionEntity buildOpen(QuizEntity quiz, QuestionSpec spec) {
        OpenQuestionEntity q = new OpenQuestionEntity();
        q.setQuiz(quiz);
        q.setPrompt(spec.prompt);
        q.setExpectedAnswer(spec.expectedAnswer);
        return questionRepository.save(q);
    }

    private QuizVersionEntity createQuizVersion(QuizEntity quiz) {
        QuizVersionEntity version = new QuizVersionEntity();
        version.setQuiz(quiz);
        version.setVersionNumber(1);
        version.setIsCurrent(true);
        quiz.getVersions().add(version);
        return quizVersionRepository.save(version);
    }

    private void simulateQuizAttempts(QuizEntity quiz, int attemptsCount) {
        Random random = new Random();
        QuizVersionEntity version = quiz.getVersions().getFirst();

        // Rango de 30 días atrás en segundos
        long secondsInLastThreeMonth = 30L * 24 * 60 * 60 * 3;

        for (int i = 1; i <= attemptsCount; i++) {
            QuizAttemptEntity attempt = new QuizAttemptEntity();
            attempt.setQuizVersion(version);

            long randomOffset = random.nextLong(secondsInLastThreeMonth);
            Instant startedAt = Instant.now().minusSeconds(randomOffset);
            Instant submittedAt = startedAt.plusSeconds(random.nextInt(600));

            attempt.setStartedAt(startedAt);
            attempt.setSubmittedAt(submittedAt);
            quizAttemptRepository.save(attempt);

            for (QuestionEntity question : quiz.getQuestions()) {
                if (question instanceof MultipleChoiceQuestionEntity mcq) {
                    MultipleChoiceAnswerEntity answer = new MultipleChoiceAnswerEntity();
                    answer.setQuizAttempt(attempt);
                    answer.setQuestion(mcq);
                    OptionEntity selected = mcq.getOptions().get(random.nextInt(mcq.getOptions().size()));
                    answer.getOptionsSelected().add(selected);
                    answer.setIsCorrect(question.validate(answer));
                    attempt.getAnswers().add(answer);
                    answerRepository.save(answer);
                } else if (question instanceof TrueOrFalseQuestionEntity tf) {
                    TrueOrFalseAnswerEntity answer = new TrueOrFalseAnswerEntity();
                    answer.setQuizAttempt(attempt);
                    answer.setQuestion(tf);
                    answer.setAnswer(random.nextBoolean());
                    answer.setIsCorrect(question.validate(answer));
                    attempt.getAnswers().add(answer);
                    answerRepository.save(answer);
                } else if (question instanceof OpenQuestionEntity open) {
                    OpenAnswerEntity answer = new OpenAnswerEntity();
                    answer.setQuizAttempt(attempt);
                    answer.setQuestion(open);
                    answer.setAnswerText("Sample answer " + i);
                    answer.setIsCorrect(question.validate(answer));
                    attempt.getAnswers().add(answer);
                    answerRepository.save(answer);
                }
            }

            attempt.setCorrectCountFromAnswers();
            quizAttemptRepository.save(attempt);
        }
    }

    // ---------------- DTOs para simplificar specs ----------------
    private record OptionSpec(String text, boolean correct) {}

    private static class QuestionSpec {
        enum Type {MCQ, TF, OPEN}
        final Type type;
        final String prompt;
        final String explanation;
        final List<OptionSpec> options;
        final Boolean correctTF;
        final String expectedAnswer;

        // Constructor para MCQ
        QuestionSpec(String prompt, String explanation, List<OptionSpec> options) {
            this.type = Type.MCQ;
            this.prompt = prompt;
            this.explanation = explanation;
            this.options = options;
            this.correctTF = null;
            this.expectedAnswer = null;
        }

        // Constructor para TF
        QuestionSpec(String prompt, String explanation, boolean correctTF) {
            this.type = Type.TF;
            this.prompt = prompt;
            this.explanation = explanation;
            this.correctTF = correctTF;
            this.options = null;
            this.expectedAnswer = null;
        }

        // Constructor para OPEN
        QuestionSpec(String prompt, String expectedAnswer) {
            this.type = Type.OPEN;
            this.prompt = prompt;
            this.expectedAnswer = expectedAnswer;
            this.explanation = null;
            this.correctTF = null;
            this.options = null;
        }
    }
}
