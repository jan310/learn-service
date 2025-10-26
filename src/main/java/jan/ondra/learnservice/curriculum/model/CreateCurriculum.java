package jan.ondra.learnservice.curriculum.model;

public record CreateCurriculum(
    CurriculumStatus status,
    String topic,
    int currentUnitNumber
) {}
