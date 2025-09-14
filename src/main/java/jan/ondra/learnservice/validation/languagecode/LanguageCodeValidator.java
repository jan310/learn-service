package jan.ondra.learnservice.validation.languagecode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class LanguageCodeValidator implements ConstraintValidator<ValidLanguageCode, String> {

    private static final Set<String> languageCodes = Arrays
        .stream(Locale.getAvailableLocales())
        .map(Locale::toLanguageTag)
        .collect(Collectors.toSet());

    @Override
    public boolean isValid(String languageCode, ConstraintValidatorContext context) {
        return languageCode != null && languageCodes.contains(languageCode);
    }

}
