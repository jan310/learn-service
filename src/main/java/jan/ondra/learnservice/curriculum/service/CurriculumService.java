package jan.ondra.learnservice.curriculum.service;

import jan.ondra.learnservice.openai.OpenAiClient;
import jan.ondra.learnservice.curriculum.model.CreateCurriculum;
import jan.ondra.learnservice.curriculum.model.CreateLearningUnit;
import jan.ondra.learnservice.curriculum.model.CurriculumIdWithUnits;
import jan.ondra.learnservice.curriculum.model.StudyContext;
import jan.ondra.learnservice.curriculum.repository.CurriculumRepository;
import jan.ondra.learnservice.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static jan.ondra.learnservice.curriculum.model.CurriculumStatus.ACTIVE;

@Service
public class CurriculumService {

    private final CurriculumRepository curriculumRepository;
    private final OpenAiClient openAiClient;

    public CurriculumService(
        CurriculumRepository curriculumRepository,
        OpenAiClient openAiClient
    ) {
        this.curriculumRepository = curriculumRepository;
        this.openAiClient = openAiClient;
    }

    @Transactional
    public CurriculumIdWithUnits createCurriculum(User user, String topic) {
        var emptyLearningUnits = openAiClient.generateEmptyLearningUnits(user.language(), topic);
        var contentOfFirstLearningUnit = openAiClient.generateLearningUnitContent(
            user.language(),
            topic,
            emptyLearningUnits.getFirst().heading(),
            emptyLearningUnits.getFirst().subheading()
        );
        var createLearningUnits = emptyLearningUnits.stream()
            .map(unit ->
                new CreateLearningUnit(
                    unit.number(),
                    unit.heading(),
                    unit.subheading(),
                    unit.number() == 1 ? contentOfFirstLearningUnit : null
                )
            )
            .toList();

        var curriculumId = curriculumRepository.persistCurriculum(
            user.id(),
            new CreateCurriculum(
                ACTIVE,
                topic,
                1
            )
        );
        curriculumRepository.persistLearningUnits(curriculumId, createLearningUnits);

        return new CurriculumIdWithUnits(curriculumId, emptyLearningUnits);
    }

    public List<StudyContext> getStudyContexts(OffsetDateTime utcDateTime) {
        return curriculumRepository.getStudyContexts(utcDateTime);
    }

    public void setStatusToFinished(List<UUID> curriculumIds) {
        curriculumRepository.setStatusToFinished(curriculumIds);
    }

    public void incrementCurrentUnitNumber(List<UUID> curriculumIds) {
        curriculumRepository.incrementCurrentUnitNumber(curriculumIds);
    }

    public void updateLearningUnitContents(Map<UUID, String> learningUnitContents) {
        curriculumRepository.updateLearningUnitContents(learningUnitContents);
    }

}
