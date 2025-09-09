package jan.ondra.learnservice.domain.user.model;

import jan.ondra.learnservice.domain.user.api.UserDTO;

import java.time.LocalTime;
import java.util.UUID;

public record User(
    UUID id,
    String authId,
    boolean notificationEnabled,
    String notificationEmail,
    LocalTime notificationTime,
    String timezone,
    String language
) {

    public User(String authId, UserDTO userDTO) {
        this(
            null,
            authId,
            userDTO.notificationEnabled(),
            userDTO.notificationEmail(),
            userDTO.notificationTime(),
            userDTO.timeZone(),
            userDTO.language()
        );
    }

}
