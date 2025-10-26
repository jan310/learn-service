package jan.ondra.learnservice.curriculum.model;

import java.util.List;
import java.util.UUID;

public record CurriculumIdWithUnits(
    UUID curriculumId,
    List<EmptyLearningUnit> emptyLearningUnits
) {}
