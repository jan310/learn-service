package jan.ondra.learnservice.domain.curriculum.persistence;

import jan.ondra.learnservice.helper.DatabaseIntegrationTest;
import jan.ondra.learnservice.curriculum.repository.CurriculumRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(CurriculumRepository.class)
class CurriculumRepositoryTest extends DatabaseIntegrationTest {

    @Autowired
    private CurriculumRepository curriculumRepository;

    @Nested
    class PersistCurriculum {

        @Test
        @DisplayName("correctly persists curriculum")
        void test1() {

        }

    }

    @Nested
    class PersistEmptyLearningUnits {}

}