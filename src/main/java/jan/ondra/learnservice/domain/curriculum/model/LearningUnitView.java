package jan.ondra.learnservice.domain.curriculum.model;

public record LearningUnitView(
    String language,
    String topic,
    String heading,
    String subheading
) {}
