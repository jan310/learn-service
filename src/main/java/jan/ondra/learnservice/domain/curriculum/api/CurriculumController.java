package jan.ondra.learnservice.domain.curriculum.api;

import jakarta.validation.Valid;
import jan.ondra.learnservice.domain.curriculum.service.CurriculumService;
import jan.ondra.learnservice.domain.curriculum.model.EmptyLearningUnit;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/curriculums")
public class CurriculumController {

    private final CurriculumService curriculumService;

    public CurriculumController(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    @PostMapping("/drafts")
    @ResponseStatus(CREATED)
    public List<EmptyLearningUnit> createDraftCurriculum(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody @Valid CurriculumCreationRequestDTO curriculumCreationRequestDTO
    ) {
        return curriculumService.createDraftCurriculum(
            jwt.getSubject(),
            curriculumCreationRequestDTO.language(),
            curriculumCreationRequestDTO.topic()
        );
    }

    @PostMapping("/drafts/accept")
    @ResponseStatus(CREATED)
    public UUID acceptDraftCurriculum(@AuthenticationPrincipal Jwt jwt) {
        return curriculumService.acceptDraftCurriculum(jwt.getSubject());
    }

    @PostMapping("/drafts/reject")
    @ResponseStatus(NO_CONTENT)
    public void rejectDraftCurriculum(@AuthenticationPrincipal Jwt jwt) {
        curriculumService.rejectDraftCurriculum(jwt.getSubject());
    }

}
