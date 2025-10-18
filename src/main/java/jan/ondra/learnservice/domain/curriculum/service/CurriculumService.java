package jan.ondra.learnservice.domain.curriculum.service;

import jan.ondra.learnservice.client.openai.OpenAiClient;
import jan.ondra.learnservice.domain.curriculum.model.CreateCurriculum;
import jan.ondra.learnservice.domain.curriculum.model.CreateLearningUnit;
import jan.ondra.learnservice.domain.curriculum.model.CurriculumIdWithUnits;
import jan.ondra.learnservice.domain.curriculum.model.LearningUnitView;
import jan.ondra.learnservice.domain.curriculum.persistence.CurriculumRepository;
import jan.ondra.learnservice.domain.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static jan.ondra.learnservice.domain.curriculum.model.CurriculumStatus.ACTIVE;

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
            new LearningUnitView(
                user.language(),
                topic,
                emptyLearningUnits.getFirst().heading(),
                emptyLearningUnits.getFirst().subheading()
            )
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
                emptyLearningUnits.size(),
                1
            )
        );
        curriculumRepository.persistLearningUnits(curriculumId, createLearningUnits);

        return new CurriculumIdWithUnits(curriculumId, emptyLearningUnits);
    }

}
