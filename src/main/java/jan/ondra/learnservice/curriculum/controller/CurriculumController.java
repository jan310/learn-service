package jan.ondra.learnservice.curriculum.controller;

import jakarta.validation.Valid;
import jan.ondra.learnservice.user.resolver.CurrentUser;
import jan.ondra.learnservice.curriculum.model.CurriculumIdWithUnits;
import jan.ondra.learnservice.curriculum.service.CurriculumService;
import jan.ondra.learnservice.user.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/curriculums")
public class CurriculumController {

    private final CurriculumService curriculumService;

    public CurriculumController(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CurriculumIdWithUnits createCurriculum(
        @CurrentUser User user,
        @RequestBody @Valid CreateCurriculumDTO createCurriculumDTO
    ) {
        return curriculumService.createCurriculum(user, createCurriculumDTO.topic());
    }

}
