package gblas.books.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {
})
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Positive(message = "Chapter number must be positive")
public @interface ValidChapterNumber {
    String message() default "Invalid Chapter Number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
