package jan.ondra.learnservice.curriculum.model;

import java.util.UUID;

public record LearningUnit(
    UUID id,
    UUID curriculumId,
    int number,
    String heading,
    String subheading,
    String content
) {}
