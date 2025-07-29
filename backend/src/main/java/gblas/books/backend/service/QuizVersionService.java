package gblas.books.backend.service;

import gblas.books.backend.entity.QuizEntity;
import gblas.books.backend.entity.QuizVersionEntity;
import gblas.books.backend.repository.QuizVersionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuizVersionService {

    private final QuizVersionRepository quizVersionRepository;

    public QuizVersionEntity getLastQuizVersionEntity(QuizEntity quizEntity) {
        return quizVersionRepository
                .findCurrentVersionByQuizId(quizEntity.getId())
                .orElseThrow(() -> new IllegalStateException("No current version found for quiz " + quizEntity.getId()));
    }
}
