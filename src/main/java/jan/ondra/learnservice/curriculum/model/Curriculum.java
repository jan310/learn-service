package jan.ondra.learnservice.curriculum.model;

import java.util.UUID;

public record Curriculum(
    UUID id,
    UUID userId,
    int queuePosition,
    String topic,
    int currentUnitNumber
) {}
