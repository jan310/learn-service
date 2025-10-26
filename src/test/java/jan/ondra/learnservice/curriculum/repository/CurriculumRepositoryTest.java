package jan.ondra.learnservice.curriculum.repository;

import jan.ondra.learnservice.helper.DatabaseIntegrationTest;
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
    class PersistCurriculumTests {

        @Test
        @DisplayName("")
        void test1() {

        }

    }

    @Nested
    class PersistLearningUnitsTests {

        @Test
        @DisplayName("")
        void test1() {

        }

    }

    @Nested
    class GetStudyContextsTests {

        @Test
        @DisplayName("")
        void test1() {

        }

    }

    @Nested
    class SetStatusToFinishedTests {

        @Test
        @DisplayName("")
        void test1() {

        }

    }

    @Nested
    class IncrementCurrentUnitNumberTests {

        @Test
        @DisplayName("")
        void test1() {

        }

    }

    @Nested
    class UpdateLearningUnitContentsTests {

        @Test
        @DisplayName("")
        void test1() {

        }

    }

}