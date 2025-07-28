package gblas.books.backend.validation;

import gblas.books.backend.entity.question.QuestionEntity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlankIfPresentValidator implements ConstraintValidator<NotBlankIfPresent, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        switch (value) {
            case null -> {
                return true;
            }
            case String str -> {
                return !str.isBlank();
            }
            case QuestionEntity.QuestionType ignored -> {
                return QuestionEntity.QuestionType.values().length > 0;
            }
            case Integer integer -> {
                return (integer > 0);
            }
            default -> {
            }
        }

        log.warn("Unsupported type for @NotBlankIfPresent: {}", value.getClass().getName());
        return false;
    }
}
