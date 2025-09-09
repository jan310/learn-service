package jan.ondra.learnservice.domain.user.api;

import jan.ondra.learnservice.domain.user.api.validation.ValidEmail;
import jan.ondra.learnservice.domain.user.api.validation.ValidLanguageCode;
import jan.ondra.learnservice.domain.user.api.validation.ValidQuarterHour;
import jan.ondra.learnservice.domain.user.api.validation.ValidTimeZone;
import jan.ondra.learnservice.domain.user.model.User;

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
) {

    public UserDTO(User user) {
        this(
            user.notificationEnabled(),
            user.notificationEmail(),
            user.notificationTime(),
            user.timezone(),
            user.language()
        );
    }

}
