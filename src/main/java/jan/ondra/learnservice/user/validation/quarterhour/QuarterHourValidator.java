package jan.ondra.learnservice.user.validation.quarterhour;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class QuarterHourValidator implements ConstraintValidator<ValidQuarterHour, LocalTime> {

    @Override
    public boolean isValid(LocalTime time, ConstraintValidatorContext context) {
        return time != null && time.getMinute() % 15 == 0;
    }

}
