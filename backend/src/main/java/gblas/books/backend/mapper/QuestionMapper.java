package gblas.books.backend.mapper;

import gblas.books.backend.dto.OpenQuestionResponse;
import gblas.books.backend.dto.QuestionResponse;
import gblas.books.backend.dto.TrueFalseQuestionResponse;
import gblas.books.backend.entity.OpenQuestionEntity;
import gblas.books.backend.entity.QuestionEntity;
import gblas.books.backend.entity.TrueOrFalseQuestionEntity;

public class QuestionMapper {

    public static QuestionResponse dtoFrom(QuestionEntity entity) {
        if (entity instanceof TrueOrFalseQuestionEntity tf) {
            return new TrueFalseQuestionResponse(
                    tf.getId(),
                    tf.getType(),
                    tf.getPrompt(),
                    tf.getExplanation(),
                    tf.getIsAnswerTrue()
            );
        } else if (entity instanceof OpenQuestionEntity op) {
            return new OpenQuestionResponse(
                    op.getId(),
                    op.getType(),
                    op.getPrompt(),
                    op.getExplanation(),
                    op.getExpectedAnswer()
            );
        } else {
            throw new IllegalArgumentException("Unknown question type: " + entity.getClass());
        }
    }
}
