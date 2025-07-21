package gblas.books.backend.mapper;

import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.entity.QuizEntity;

import java.util.stream.Collectors;

public class QuizMapper {
    public static QuizResponse dtoFrom(QuizEntity quiz) {
        return new QuizResponse(
                quiz.getId(),
                ChapterMapper.dtoFrom(quiz.getChapter()),
                quiz.getQuestions()
                        .stream()
                        .map(QuestionMapper::dtoFrom)
                        .collect(Collectors.toList())
        );
    }
}
