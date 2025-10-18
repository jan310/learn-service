package jan.ondra.learnservice.domain.user.api;

import jan.ondra.learnservice.validation.email.ValidEmail;
import jan.ondra.learnservice.validation.languagecode.ValidLanguageCode;
import jan.ondra.learnservice.validation.quarterhour.ValidQuarterHour;
import jan.ondra.learnservice.validation.timezone.ValidTimeZone;

import java.time.LocalTime;

public record UserDTO(
    boolean notificationEnabled,

    @ValidEmail
    String notificationEmail,

    @ValidQuarterHour
    LocalTime notificationTime,

    @ValidTimeZone
    String timeZone,

    @ValidLanguageCode
    String language
) {}
