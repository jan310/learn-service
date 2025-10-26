package jan.ondra.learnservice.curriculum.controller;

import jakarta.validation.constraints.NotBlank;

public record CreateCurriculumDTO(@NotBlank String topic) {}
