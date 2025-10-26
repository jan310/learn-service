package jan.ondra.learnservice.user.controller;

import jan.ondra.learnservice.user.validation.email.ValidEmail;
import jan.ondra.learnservice.user.validation.languagecode.ValidLanguageCode;
import jan.ondra.learnservice.user.validation.quarterhour.ValidQuarterHour;
import jan.ondra.learnservice.user.validation.timezone.ValidTimeZone;

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
