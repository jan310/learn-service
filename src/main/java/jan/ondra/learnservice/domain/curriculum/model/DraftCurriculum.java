package jan.ondra.learnservice.domain.curriculum.model;

import java.util.List;

public record DraftCurriculum(
    String topic,
    List<EmptyLearningUnit> emptyLearningUnits
) {}
