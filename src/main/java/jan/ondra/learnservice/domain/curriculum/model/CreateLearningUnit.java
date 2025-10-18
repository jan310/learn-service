package jan.ondra.learnservice.domain.curriculum.model;

public record CreateLearningUnit(
    int number,
    String heading,
    String subheading,
    String content
) {}
