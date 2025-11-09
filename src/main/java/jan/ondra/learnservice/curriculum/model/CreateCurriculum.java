package jan.ondra.learnservice.curriculum.model;

public record CreateCurriculum(
    int queuePosition,
    String topic,
    int currentUnitNumber
) {}
