package gblas.books.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Title is required")
public @interface ValidTitle {
    String message() default "Invalid Title";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
