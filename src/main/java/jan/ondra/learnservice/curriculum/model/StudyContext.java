package jan.ondra.learnservice.curriculum.model;

import java.util.UUID;

public record StudyContext(
    UUID userId,
    String email,
    String language,
    UUID curriculumId,
    String topic,
    String currentContent,
    UUID nextUnitId,
    String nextHeading,
    String nextSubheading
) {}
