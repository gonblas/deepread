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
        UserEntity demoUser = new UserEntity();
        demoUser.setEmail("demo@example.com");
        demoUser.setUsername("DemoUser");
        demoUser.setHashedPassword(passwordEncoder.encode("password"));
        demoUser = userRepository.save(demoUser);

        BookEntity book1 = new BookEntity();
        book1.setOwner(demoUser);
        book1.setTitle("Clean Code");
        book1.setDescription("A Handbook of Agile Software Craftsmanship");
        book1.setGenre(BookEntity.BookGenre.PROGRAMMING);
        book1.getAuthors().add("Robert C. Martin");
        bookRepository.save(book1);

        BookEntity book2 = new BookEntity();
        book2.setOwner(demoUser);
        book2.setTitle("The Pragmatic Programmer");
        book2.setDescription("Your Journey to Mastery");
        book2.setGenre(BookEntity.BookGenre.PROGRAMMING);
        book2.getAuthors().add("Andrew Hunt");
        book2.getAuthors().add("David Thomas");
        bookRepository.save(book2);

        BookEntity book3 = new BookEntity();
        book3.setOwner(demoUser);
        book3.setTitle("Design Patterns");
        book3.setDescription("Elements of Reusable Object-Oriented Software");
        book3.setGenre(BookEntity.BookGenre.PROGRAMMING);
        book3.getAuthors().add("Erich Gamma");
        book3.getAuthors().add("Richard Helm");
        book3.getAuthors().add("Ralph Johnson");
        book3.getAuthors().add("John Vlissides");
        bookRepository.save(book3);

        BookEntity book4 = new BookEntity();
        book4.setOwner(demoUser);
        book4.setTitle("Effective Java");
        book4.setDescription("Best practices for Java programming");
        book4.setGenre(BookEntity.BookGenre.PROGRAMMING);
        book4.getAuthors().add("Joshua Bloch");
        bookRepository.save(book4);

        BookEntity book5 = new BookEntity();
        book5.setOwner(demoUser);
        book5.setTitle("Capital in the Twenty-First Century");
        book5.setDescription("Analysis of wealth and inequality");
        book5.setGenre(BookEntity.BookGenre.ECONOMICS);
        book5.getAuthors().add("Thomas Piketty");
        bookRepository.save(book5);

        BookEntity book6 = new BookEntity();
        book6.setOwner(demoUser);
        book6.setTitle("Freakonomics");
        book6.setDescription("A Rogue Economist Explores the Hidden Side of Everything");
        book6.setGenre(BookEntity.BookGenre.ECONOMICS);
        book6.getAuthors().add("Steven D. Levitt");
        book6.getAuthors().add("Stephen J. Dubner");
        bookRepository.save(book6);

        BookEntity book7 = new BookEntity();
        book7.setOwner(demoUser);
        book7.setTitle("The Wealth of Nations");
        book7.setDescription("Classic work on economics");
        book7.setGenre(BookEntity.BookGenre.ECONOMICS);
        book7.getAuthors().add("Adam Smith");
        bookRepository.save(book7);

        BookEntity book8 = new BookEntity();
        book8.setOwner(demoUser);
        book8.setTitle("Thinking, Fast and Slow");
        book8.setDescription("Insights on human behavior and economics");
        book8.setGenre(BookEntity.BookGenre.ECONOMICS);
        book8.getAuthors().add("Daniel Kahneman");
        bookRepository.save(book8);

        BookEntity book9 = new BookEntity();
        book9.setOwner(demoUser);
        book9.setTitle("Meditations");
        book9.setDescription("Thoughts of Marcus Aurelius");
        book9.setGenre(BookEntity.BookGenre.PHILOSOPHY);
        book9.getAuthors().add("Marcus Aurelius");
        bookRepository.save(book9);

        BookEntity book10 = new BookEntity();
        book10.setOwner(demoUser);
        book10.setTitle("The Republic");
        book10.setDescription("Plato's vision of justice and society");
        book10.setGenre(BookEntity.BookGenre.PHILOSOPHY);
        book10.getAuthors().add("Plato");
        bookRepository.save(book10);

        BookEntity book11 = new BookEntity();
        book11.setOwner(demoUser);
        book11.setTitle("Beyond Good and Evil");
        book11.setDescription("Critique of morality");
        book11.setGenre(BookEntity.BookGenre.PHILOSOPHY);
        book11.getAuthors().add("Friedrich Nietzsche");
        bookRepository.save(book11);

        BookEntity book12 = new BookEntity();
        book12.setOwner(demoUser);
        book12.setTitle("Critique of Pure Reason");
        book12.setDescription("On human knowledge");
        book12.setGenre(BookEntity.BookGenre.PHILOSOPHY);
        book12.getAuthors().add("Immanuel Kant");
        bookRepository.save(book12);

        BookEntity book13 = new BookEntity();
        book13.setOwner(demoUser);
        book13.setTitle("Man's Search for Meaning");
        book13.setDescription("Finding purpose in life");
        book13.setGenre(BookEntity.BookGenre.PSYCHOLOGY);
        book13.getAuthors().add("Viktor Frankl");
        bookRepository.save(book13);

        BookEntity book14 = new BookEntity();
        book14.setOwner(demoUser);
        book14.setTitle("The Power of Habit");
        book14.setDescription("Understanding habit formation");
        book14.setGenre(BookEntity.BookGenre.PSYCHOLOGY);
        book14.getAuthors().add("Charles Duhigg");
        bookRepository.save(book14);

        BookEntity book15 = new BookEntity();
        book15.setOwner(demoUser);
        book15.setTitle("Thinking, Fast and Slow");
        book15.setDescription("Cognitive biases and decision making");
        book15.setGenre(BookEntity.BookGenre.PSYCHOLOGY);
        book15.getAuthors().add("Daniel Kahneman");
        bookRepository.save(book15);

        // Book 1: Clean Code
        ChapterEntity chapter1_1 = new ChapterEntity();
        chapter1_1.setBook(book1);
        chapter1_1.setNumber(1);
        chapter1_1.setTitle("Meaningful Names");
        chapter1_1.setSummary("How to use clear and meaningful names for variables, functions, and classes.");
        chapterRepository.save(chapter1_1);

        ChapterEntity chapter1_2 = new ChapterEntity();
        chapter1_2.setBook(book1);
        chapter1_2.setNumber(2);
        chapter1_2.setTitle("Functions");
        chapter1_2.setSummary("Writing small, readable, and well-structured functions.");
        chapterRepository.save(chapter1_2);

        ChapterEntity chapter1_3 = new ChapterEntity();
        chapter1_3.setBook(book1);
        chapter1_3.setNumber(3);
        chapter1_3.setTitle("Comments");
        chapter1_3.setSummary("When and how to use comments effectively.");
        chapterRepository.save(chapter1_3);

        // Book 2: The Pragmatic Programmer
        ChapterEntity chapter2_1 = new ChapterEntity();
        chapter2_1.setBook(book2);
        chapter2_1.setNumber(1);
        chapter2_1.setTitle("A Pragmatic Philosophy");
        chapter2_1.setSummary("Introduction to the pragmatic programming philosophy.");
        chapterRepository.save(chapter2_1);

        ChapterEntity chapter2_2 = new ChapterEntity();
        chapter2_2.setBook(book2);
        chapter2_2.setNumber(2);
        chapter2_2.setTitle("A Pragmatic Approach");
        chapter2_2.setSummary("How to approach complex problems pragmatically.");
        chapterRepository.save(chapter2_2);

        ChapterEntity chapter2_3 = new ChapterEntity();
        chapter2_3.setBook(book2);
        chapter2_3.setNumber(3);
        chapter2_3.setTitle("The Basic Tools");
        chapter2_3.setSummary("Essential tools for a pragmatic programmer.");
        chapterRepository.save(chapter2_3);

        // Book 3: Design Patterns
        ChapterEntity chapter3_1 = new ChapterEntity();
        chapter3_1.setBook(book3);
        chapter3_1.setNumber(1);
        chapter3_1.setTitle("Creational Patterns");
        chapter3_1.setSummary("Object creation patterns such as Singleton and Factory.");
        chapterRepository.save(chapter3_1);

        ChapterEntity chapter3_2 = new ChapterEntity();
        chapter3_2.setBook(book3);
        chapter3_2.setNumber(2);
        chapter3_2.setTitle("Structural Patterns");
        chapter3_2.setSummary("Patterns that organize classes and objects, such as Adapter and Composite.");
        chapterRepository.save(chapter3_2);

        ChapterEntity chapter3_3 = new ChapterEntity();
        chapter3_3.setBook(book3);
        chapter3_3.setNumber(3);
        chapter3_3.setTitle("Behavioral Patterns");
        chapter3_3.setSummary("Patterns that define communication between objects, such as Observer and Strategy.");
        chapterRepository.save(chapter3_3);

        // Book 4: Effective Java
        ChapterEntity chapter4_1 = new ChapterEntity();
        chapter4_1.setBook(book4);
        chapter4_1.setNumber(1);
        chapter4_1.setTitle("Creating and Destroying Objects");
        chapter4_1.setSummary("Best practices for creating and destroying objects in Java.");
        chapterRepository.save(chapter4_1);

        ChapterEntity chapter4_2 = new ChapterEntity();
        chapter4_2.setBook(book4);
        chapter4_2.setNumber(2);
        chapter4_2.setTitle("Methods Common to All Objects");
        chapter4_2.setSummary("Proper use of methods like equals, hashCode, and toString.");
        chapterRepository.save(chapter4_2);

        ChapterEntity chapter4_3 = new ChapterEntity();
        chapter4_3.setBook(book4);
        chapter4_3.setNumber(3);
        chapter4_3.setTitle("Generics");
        chapter4_3.setSummary("How to use generics correctly for safety and clarity.");
        chapterRepository.save(chapter4_3);

        // Book 5: Capital in the Twenty-First Century
        ChapterEntity chapter5_1 = new ChapterEntity();
        chapter5_1.setBook(book5);
        chapter5_1.setNumber(1);
        chapter5_1.setTitle("Income and Capital");
        chapter5_1.setSummary("Introduction to the relationship between income and capital.");
        chapterRepository.save(chapter5_1);

        ChapterEntity chapter5_2 = new ChapterEntity();
        chapter5_2.setBook(book5);
        chapter5_2.setNumber(2);
        chapter5_2.setTitle("Growth and Inequality");
        chapter5_2.setSummary("How economic growth affects inequality.");
        chapterRepository.save(chapter5_2);

        ChapterEntity chapter5_3 = new ChapterEntity();
        chapter5_3.setBook(book5);
        chapter5_3.setNumber(3);
        chapter5_3.setTitle("Capital in History");
        chapter5_3.setSummary("The history of capital and its impact on the modern economy.");
        chapterRepository.save(chapter5_3);

        // Book 6: Freakonomics
        ChapterEntity chapter6_1 = new ChapterEntity();
        chapter6_1.setBook(book6);
        chapter6_1.setNumber(1);
        chapter6_1.setTitle("What Do Schoolteachers and Sumo Wrestlers Have in Common?");
        chapter6_1.setSummary("Exploration of incentives and human motivations.");
        chapterRepository.save(chapter6_1);

        ChapterEntity chapter6_2 = new ChapterEntity();
        chapter6_2.setBook(book6);
        chapter6_2.setNumber(2);
        chapter6_2.setTitle("How is the Ku Klux Klan Like a Group of Real-Estate Agents?");
        chapter6_2.setSummary("Analysis of asymmetric information and its societal impact.");
        chapterRepository.save(chapter6_2);

        ChapterEntity chapter6_3 = new ChapterEntity();
        chapter6_3.setBook(book6);
        chapter6_3.setNumber(3);
        chapter6_3.setTitle("Why Do Drug Dealers Still Live with Their Moms?");
        chapter6_3.setSummary("Investigation of informal economy and human behavior.");
        chapterRepository.save(chapter6_3);

        QuizEntity quiz1 = new QuizEntity();
        quiz1.setChapter(chapter1_1);
        quizRepository.save(quiz1);
        // MCQ
        MultipleChoiceQuestionEntity mcq1 = new MultipleChoiceQuestionEntity();
        mcq1.setQuiz(quiz1);
        mcq1.setPrompt("Which of the following is NOT a characteristic of clean code?");
        mcq1.setExplanation("Clean code should be readable, simple, and maintainable.");
        OptionEntity q1o1 = new OptionEntity(); q1o1.setText("Readable"); q1o1.setIsCorrect(false); q1o1.setQuestion(mcq1);
        OptionEntity q1o2 = new OptionEntity(); q1o2.setText("Complicated"); q1o2.setIsCorrect(true); q1o2.setQuestion(mcq1);
        OptionEntity q1o3 = new OptionEntity(); q1o3.setText("Maintainable"); q1o3.setIsCorrect(false); q1o3.setQuestion(mcq1);
        mcq1.getOptions().add(q1o1); mcq1.getOptions().add(q1o2); mcq1.getOptions().add(q1o3);
        questionRepository.save(mcq1);

        // True/False
        TrueOrFalseQuestionEntity tf1 = new TrueOrFalseQuestionEntity();
        tf1.setQuiz(quiz1);
        tf1.setPrompt("Meaningful names improve code readability.");
        tf1.setIsAnswerTrue(true);
        tf1.setExplanation("Descriptive names help developers understand the code quickly.");
        questionRepository.save(tf1);

        // Open
        OpenQuestionEntity open1 = new OpenQuestionEntity();
        open1.setQuiz(quiz1);
        open1.setPrompt("Explain why small functions are easier to maintain.");
        open1.setExpectedAnswer("Small functions reduce complexity, making code easier to read, test, and debug.");
        questionRepository.save(open1);

        QuizVersionEntity version1 = createQuizVersion(quiz1);

        // Add to quiz
        version1.getQuestions().add(mcq1); version1.getQuestions().add(tf1); version1.getQuestions().add(open1);
        quiz1.getQuestions().add(mcq1); quiz1.getQuestions().add(tf1); quiz1.getQuestions().add(open1);
        quizRepository.save(quiz1);
        // ----------------- Book 2: The Pragmatic Programmer, Chapter 1 -----------------
        QuizEntity quiz2 = new QuizEntity();
        quiz2.setChapter(chapter2_1);
        quizRepository.save(quiz2);
        // MCQ
        MultipleChoiceQuestionEntity mcq2 = new MultipleChoiceQuestionEntity();
        mcq2.setQuiz(quiz2);
        mcq2.setPrompt("What is the primary focus of pragmatic programming?");
        mcq2.setExplanation("Pragmatic programming emphasizes adaptability, learning, and practical solutions.");
        OptionEntity q2o1 = new OptionEntity(); q2o1.setText("Practical and adaptable solutions"); q2o1.setIsCorrect(true); q2o1.setQuestion(mcq2);
        OptionEntity q2o2 = new OptionEntity(); q2o2.setText("Avoiding all changes"); q2o2.setIsCorrect(false); q2o2.setQuestion(mcq2);
        OptionEntity q2o3 = new OptionEntity(); q2o3.setText("Strict adherence to rules"); q2o3.setIsCorrect(false); q2o3.setQuestion(mcq2);
        mcq2.getOptions().add(q2o1); mcq2.getOptions().add(q2o2); mcq2.getOptions().add(q2o3);
        questionRepository.save(mcq2);

        // True/False
        TrueOrFalseQuestionEntity tf2 = new TrueOrFalseQuestionEntity();
        tf2.setQuiz(quiz2);
        tf2.setPrompt("Pragmatic programmers value learning from mistakes.");
        tf2.setIsAnswerTrue(true);
        questionRepository.save(tf2);

        // Open
        OpenQuestionEntity open2 = new OpenQuestionEntity();
        open2.setQuiz(quiz2);
        open2.setPrompt("Name one tool that is essential for a pragmatic programmer.");
        open2.setExpectedAnswer("Version control system (like Git), automated testing tools, or debugger.");
        questionRepository.save(open2);

        QuizVersionEntity version2 = createQuizVersion(quiz2);

        // Add to quiz
        version2.getQuestions().add(mcq2); version2.getQuestions().add(tf2); version2.getQuestions().add(open2);
        quiz2.getQuestions().add(mcq2); quiz2.getQuestions().add(tf2); quiz2.getQuestions().add(open2);
        quizRepository.save(quiz2);
        // ----------------- Book 3: Design Patterns, Chapter 1 -----------------
        QuizEntity quiz3 = new QuizEntity();
        quiz3.setChapter(chapter3_1);
        quizRepository.save(quiz3);
        // MCQ
        MultipleChoiceQuestionEntity mcq3 = new MultipleChoiceQuestionEntity();
        mcq3.setQuiz(quiz3);
        mcq3.setPrompt("Which design pattern provides a way to create objects without specifying their concrete classes?");
        mcq3.setExplanation("Factory pattern allows creation of objects without coupling to specific classes.");
        OptionEntity q3o1 = new OptionEntity(); q3o1.setText("Observer"); q3o1.setIsCorrect(false); q3o1.setQuestion(mcq3);
        OptionEntity q3o2 = new OptionEntity(); q3o2.setText("Factory"); q3o2.setIsCorrect(true); q3o2.setQuestion(mcq3);
        OptionEntity q3o3 = new OptionEntity(); q3o3.setText("Singleton"); q3o3.setIsCorrect(false); q3o3.setQuestion(mcq3);
        mcq3.getOptions().add(q3o1); mcq3.getOptions().add(q3o2); mcq3.getOptions().add(q3o3);
        questionRepository.save(mcq3);

        // True/False
        TrueOrFalseQuestionEntity tf3 = new TrueOrFalseQuestionEntity();
        tf3.setQuiz(quiz3);
        tf3.setPrompt("The Singleton pattern ensures only one instance of a class exists.");
        tf3.setIsAnswerTrue(true);
        questionRepository.save(tf3);

        // Open
        OpenQuestionEntity open3 = new OpenQuestionEntity();
        open3.setQuiz(quiz3);
        open3.setPrompt("Explain the advantage of using the Strategy pattern.");
        open3.setExpectedAnswer("It allows selecting an algorithm at runtime, making code flexible and maintainable.");
        questionRepository.save(open3);

        QuizVersionEntity version3 = createQuizVersion(quiz3);

        version3.getQuestions().add(mcq3); version3.getQuestions().add(tf3); version3.getQuestions().add(open3);
        quiz3.getQuestions().add(mcq3); quiz3.getQuestions().add(tf3); quiz3.getQuestions().add(open3);
        quizRepository.save(quiz3);
        // ----------------- Book 4: Effective Java, Chapter 1 -----------------
        QuizEntity quiz4 = new QuizEntity();
        quiz4.setChapter(chapter4_1);
        quizRepository.save(quiz4);
        // MCQ
        MultipleChoiceQuestionEntity mcq4 = new MultipleChoiceQuestionEntity();
        mcq4.setQuiz(quiz4);
        mcq4.setPrompt("Which of the following is recommended in Effective Java for object creation?");
        mcq4.setExplanation("Using static factory methods can improve readability and flexibility.");
        OptionEntity q4o1 = new OptionEntity(); q4o1.setText("Use public constructors only"); q4o1.setIsCorrect(false); q4o1.setQuestion(mcq4);
        OptionEntity q4o2 = new OptionEntity(); q4o2.setText("Use static factory methods"); q4o2.setIsCorrect(true); q4o2.setQuestion(mcq4);
        OptionEntity q4o3 = new OptionEntity(); q4o3.setText("Avoid using any design patterns"); q4o3.setIsCorrect(false); q4o3.setQuestion(mcq4);
        mcq4.getOptions().add(q4o1); mcq4.getOptions().add(q4o2); mcq4.getOptions().add(q4o3);
        questionRepository.save(mcq4);

        // True/False
        TrueOrFalseQuestionEntity tf4 = new TrueOrFalseQuestionEntity();
        tf4.setQuiz(quiz4);
        tf4.setPrompt("Immutable objects are recommended when possible.");
        tf4.setIsAnswerTrue(true);
        questionRepository.save(tf4);

        // Open
        OpenQuestionEntity open4 = new OpenQuestionEntity();
        open4.setQuiz(quiz4);
        open4.setPrompt("Explain why overriding equals() and hashCode() consistently is important.");
        open4.setExpectedAnswer("It ensures correct behavior in collections and when comparing objects.");
        questionRepository.save(open4);

        QuizVersionEntity version4 = createQuizVersion(quiz4);

        version4.getQuestions().add(mcq4); version4.getQuestions().add(tf4); version4.getQuestions().add(open4);
        quiz4.getQuestions().add(mcq4); quiz4.getQuestions().add(tf4); quiz4.getQuestions().add(open4);
        quizRepository.save(quiz4);
        // ----------------- Book 5: Capital in the Twenty-First Century, Chapter 1 -----------------
        QuizEntity quiz5 = new QuizEntity();
        quiz5.setChapter(chapter5_1);
        quizRepository.save(quiz5);
        // MCQ
        MultipleChoiceQuestionEntity mcq5 = new MultipleChoiceQuestionEntity();
        mcq5.setQuiz(quiz5);
        mcq5.setPrompt("What is the main subject of Capital in the Twenty-First Century?");
        mcq5.setExplanation("The book analyzes wealth inequality and its evolution over time.");
        OptionEntity q5o1 = new OptionEntity(); q5o1.setText("Climate change"); q5o1.setIsCorrect(false); q5o1.setQuestion(mcq5);
        OptionEntity q5o2 = new OptionEntity(); q5o2.setText("Wealth inequality"); q5o2.setIsCorrect(true); q5o2.setQuestion(mcq5);
        OptionEntity q5o3 = new OptionEntity(); q5o3.setText("Quantum physics"); q5o3.setIsCorrect(false); q5o3.setQuestion(mcq5);
        mcq5.getOptions().add(q5o1); mcq5.getOptions().add(q5o2); mcq5.getOptions().add(q5o3);
        questionRepository.save(mcq5);

        // True/False
        TrueOrFalseQuestionEntity tf5 = new TrueOrFalseQuestionEntity();
        tf5.setQuiz(quiz5);
        tf5.setPrompt("The book shows that wealth concentration can grow faster than economic growth.");
        tf5.setIsAnswerTrue(true);
        questionRepository.save(tf5);

        // Open
        OpenQuestionEntity open5 = new OpenQuestionEntity();
        open5.setQuiz(quiz5);
        open5.setPrompt("Name one historical example of extreme wealth concentration mentioned in the book.");
        open5.setExpectedAnswer("19th-century Europe, United States Gilded Age, or similar examples.");
        questionRepository.save(open5);

        QuizVersionEntity version5 = createQuizVersion(quiz5);

        version5.getQuestions().add(mcq5); version5.getQuestions().add(tf5); version5.getQuestions().add(open5);
        quiz5.getQuestions().add(mcq5); quiz5.getQuestions().add(tf5); quiz5.getQuestions().add(open5);
        quizRepository.save(quiz5);
        // ----------------- Book 6: Freakonomics, Chapter 1 -----------------
        QuizEntity quiz6 = new QuizEntity();
        quiz6.setChapter(chapter6_1);
        quizRepository.save(quiz6);
        // MCQ
        MultipleChoiceQuestionEntity mcq6 = new MultipleChoiceQuestionEntity();
        mcq6.setQuiz(quiz6);
        mcq6.setPrompt("Which approach does Freakonomics use to analyze everyday phenomena?");
        mcq6.setExplanation("The book uses economic reasoning to explore hidden incentives.");
        OptionEntity q6o1 = new OptionEntity(); q6o1.setText("Philosophical analysis"); q6o1.setIsCorrect(false); q6o1.setQuestion(mcq6);
        OptionEntity q6o2 = new OptionEntity(); q6o2.setText("Economic analysis of incentives"); q6o2.setIsCorrect(true); q6o2.setQuestion(mcq6);
        OptionEntity q6o3 = new OptionEntity(); q6o3.setText("Random guessing"); q6o3.setIsCorrect(false); q6o3.setQuestion(mcq6);
        mcq6.getOptions().add(q6o1); mcq6.getOptions().add(q6o2); mcq6.getOptions().add(q6o3);
        questionRepository.save(mcq6);

        // True/False
        TrueOrFalseQuestionEntity tf6 = new TrueOrFalseQuestionEntity();
        tf6.setQuiz(quiz6);
        tf6.setPrompt("Freakonomics explores hidden motives behind human behavior.");
        tf6.setIsAnswerTrue(true);
        questionRepository.save(tf6);

        // Open
        OpenQuestionEntity open6 = new OpenQuestionEntity();
        open6.setQuiz(quiz6);
        open6.setPrompt("Give one example of an unusual economic phenomenon discussed in the book.");
        open6.setExpectedAnswer("Cheating teachers, sumo wrestlers match-fixing, or crime rates related to abortion legalization.");
        questionRepository.save(open6);

        QuizVersionEntity version6 = createQuizVersion(quiz6);

        version6.getQuestions().add(mcq6); version6.getQuestions().add(tf6); version6.getQuestions().add(open6);
        quiz6.getQuestions().add(mcq6); quiz6.getQuestions().add(tf6); quiz6.getQuestions().add(open6);
        quizRepository.save(quiz6);

        Random random = new Random();

        for (QuizEntity quiz : new QuizEntity[]{quiz1, quiz2, quiz3, quiz4, quiz5, quiz6}) {
            QuizVersionEntity version = quiz.getVersions().getFirst();

            for (int i = 1; i <= 3; i++) {
                QuizAttemptEntity attempt = new QuizAttemptEntity();
                attempt.setQuizVersion(version);
                attempt.setStartedAt(Instant.now().minusSeconds(random.nextInt(3600)));
                attempt.setSubmittedAt(Instant.now());
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

    }

    private QuizVersionEntity createQuizVersion(QuizEntity quiz) {
        QuizVersionEntity version = new QuizVersionEntity();
        version.setQuiz(quiz);
        version.setVersionNumber(1);
        version.setIsCurrent(true);
        quiz.getVersions().add(version);
        quizVersionRepository.save(version);
        return version;
    }



}
