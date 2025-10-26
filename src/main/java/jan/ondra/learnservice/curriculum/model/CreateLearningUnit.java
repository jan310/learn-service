package jan.ondra.learnservice.curriculum.model;

public record CreateLearningUnit(
    int number,
    String heading,
    String subheading,
    String content
) {}
