package gblas.books.backend.service;

import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.QuizVersionEntity;
import gblas.books.backend.repository.QuizRepository;
import gblas.books.backend.repository.QuizVersionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class QuizVersionService {

    private final QuizVersionRepository quizVersionRepository;

    public QuizVersionEntity getLastQuizVersionEntity(QuizEntity quizEntity) {
        return quizVersionRepository
                .findCurrentVersionByQuizId(quizEntity.getId())
                .orElseThrow(() -> new IllegalStateException("No current version found for quiz " + quizEntity.getId()));
    }

    public QuizVersionEntity updateVersion(QuizEntity quiz) {
        QuizVersionEntity oldQuizVersion = getLastQuizVersionEntity(quiz);
        QuizVersionEntity newQuizVersion = cloneQuizVersion(oldQuizVersion);
        oldQuizVersion.setIsCurrent(false);
        newQuizVersion.updateVersionNumber();
        quizVersionRepository.save(oldQuizVersion);
        quizVersionRepository.save(newQuizVersion);
        return newQuizVersion;
    }

    private QuizVersionEntity cloneQuizVersion(QuizVersionEntity oldVersion) {
        QuizVersionEntity newVersion = new QuizVersionEntity();

        newVersion.setQuiz(oldVersion.getQuiz());
        newVersion.setIsCurrent(true);
        newVersion.setVersionNumber(oldVersion.getVersionNumber());

        newVersion.setQuestions(new ArrayList<>(oldVersion.getQuestions()));

        return newVersion;
    }

}
