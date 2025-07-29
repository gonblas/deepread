package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.question.QuestionRequest;
import gblas.books.backend.dto.question.QuestionResponse;
import gblas.books.backend.entity.question.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionMapperFactory {

    private final List<TypedQuestionMapper<?, ?, ?>> mappers;

    @SuppressWarnings("unchecked")
    public TypedQuestionMapper<? extends QuestionRequest, ? extends QuestionEntity, ? extends QuestionResponse> getByType(QuestionEntity.QuestionType type) {
        return mappers.stream()
                .filter(m -> m.getQuestionType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No mapper for type " + type));
    }

}

