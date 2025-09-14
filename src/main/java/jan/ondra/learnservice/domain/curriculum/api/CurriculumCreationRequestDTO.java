package jan.ondra.learnservice.domain.curriculum.api;

import jakarta.validation.constraints.NotBlank;
import jan.ondra.learnservice.validation.languagecode.ValidLanguageCode;

public record CurriculumCreationRequestDTO(
    @ValidLanguageCode
    String language,

    @NotBlank
    String topic
) {}
