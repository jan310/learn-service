package jan.ondra.learnservice.user.model;

import java.time.LocalTime;
import java.util.UUID;

public record User(
    UUID id,
    String authSubject,
    boolean notificationEnabled,
    String notificationEmail,
    LocalTime notificationTime,
    String timeZone,
    String language
) {}
