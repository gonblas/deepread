package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.AnswerResponse;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnswerMapperFactory {

    private final List<TypedAnswerMapper<?, ?, ?>> mappers;

    @SuppressWarnings("unchecked")
    public TypedAnswerMapper<? extends AnswerRequest, ? extends AnswerEntity, ? extends AnswerResponse> getByType(QuestionEntity.QuestionType type) {
        return mappers.stream()
                .filter(m -> m.getAnswerType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No mapper for type " + type));
    }

}

