package jan.ondra.learnservice.domain.curriculum.model;

import java.util.UUID;

public record Curriculum(
    UUID id,
    UUID userId,
    CurriculumStatus status,
    String topic,
    int numberOfUnits,
    int currentUnitNumber
) {

    public Curriculum(
        UUID userId,
        CurriculumStatus status,
        String topic,
        int numberOfUnits,
        int currentUnitNumber
    ) {
        this(null, userId, status, topic, numberOfUnits, currentUnitNumber);
    }

}
