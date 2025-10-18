package jan.ondra.learnservice.domain.curriculum.api;

import jakarta.validation.constraints.NotBlank;

public record CreateCurriculumDTO(@NotBlank String topic) {}
