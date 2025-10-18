package jan.ondra.learnservice.domain.curriculum.model;

public record CreateCurriculum(
    CurriculumStatus status,
    String topic,
    int numberOfUnits,
    int currentUnitNumber
) {}
