package jan.ondra.learnservice.domain.user.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LanguageCodeValidator.class)
    public @interface ValidLanguageCode {
    String message() default "must be a well-formed IETF BCP 47 language tag";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
