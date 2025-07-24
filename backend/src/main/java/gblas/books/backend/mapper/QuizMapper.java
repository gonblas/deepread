package gblas.books.backend.mapper;

import gblas.books.backend.dto.QuizResponse;
import gblas.books.backend.entity.QuizEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuizMapper {

    private final QuestionMapper questionMapper;

    public QuizResponse dtoFrom(QuizEntity quiz) {
        return new QuizResponse(
                quiz.getId(),
                ChapterMapper.INSTANCE.toDto(quiz.getChapter()),
                quiz.getQuestions()
                        .stream()
                        .map(questionMapper::dtoFrom)
                        .collect(Collectors.toList())
        );
    }
}
