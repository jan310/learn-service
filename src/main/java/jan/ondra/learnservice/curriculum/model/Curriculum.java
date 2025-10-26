package jan.ondra.learnservice.curriculum.model;

import java.util.UUID;

public record Curriculum(
    UUID id,
    UUID userId,
    CurriculumStatus status,
    String topic,
    int currentUnitNumber
) {}
