package jan.ondra.learnservice.user.model;

import java.time.LocalTime;

public record ModifyUser(
    String authSubject,
    boolean notificationEnabled,
    String notificationEmail,
    LocalTime notificationTime,
    String timeZone,
    String language
) {}
