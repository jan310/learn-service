package jan.ondra.learnservice.domain.curriculum.service;

import jan.ondra.learnservice.client.OpenAiClient;
import jan.ondra.learnservice.domain.curriculum.model.Curriculum;
import jan.ondra.learnservice.domain.curriculum.model.DraftCurriculum;
import jan.ondra.learnservice.domain.curriculum.persistence.CurriculumRepository;
import jan.ondra.learnservice.domain.curriculum.model.EmptyLearningUnit;
import jan.ondra.learnservice.domain.user.service.UserService;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static jan.ondra.learnservice.domain.curriculum.model.CurriculumStatus.ACTIVE;

@Service
public class CurriculumService {

    private final CurriculumRepository curriculumRepository;
    private final UserService userService;
    private final OpenAiClient openAiClient;
    private final Cache curriculumCache;

    public CurriculumService(
        CurriculumRepository curriculumRepository,
        UserService userService,
        OpenAiClient openAiClient,
        Cache curriculumCache
    ) {
        this.curriculumRepository = curriculumRepository;
        this.userService = userService;
        this.openAiClient = openAiClient;
        this.curriculumCache = curriculumCache;
    }

    public List<EmptyLearningUnit> createDraftCurriculum(String authId, String language, String topic) {
        var emptyLearningUnits = openAiClient.generateEmptyLearningUnits(language, topic);
        curriculumCache.put(authId, new DraftCurriculum(topic, emptyLearningUnits));
        return emptyLearningUnits;
    }

    @Transactional
    public UUID acceptDraftCurriculum(String authId) {
        var draftCurriculum = curriculumCache.get(authId, DraftCurriculum.class);
        if (draftCurriculum == null) {
            throw new RuntimeException("No draft curriculum found for authId: " + authId);
        }

        curriculumCache.evict(authId);

        var userId = userService.getUserIdByAuthId(authId);
        var curriculumId = curriculumRepository.persistCurriculum(
            new Curriculum(
                userId,
                ACTIVE,
                draftCurriculum.topic(),
                draftCurriculum.emptyLearningUnits().size(),
                1
            )
        );
        curriculumRepository.persistEmptyLearningUnits(
            curriculumId,
            draftCurriculum.emptyLearningUnits()
        );

        return curriculumId;
    }

    public void rejectDraftCurriculum(String authId) {
        curriculumCache.evict(authId);
    }

}
