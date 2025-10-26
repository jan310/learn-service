package jan.ondra.learnservice.curriculum.repository;

import jan.ondra.learnservice.curriculum.model.CreateCurriculum;
import jan.ondra.learnservice.curriculum.model.CreateLearningUnit;
import jan.ondra.learnservice.curriculum.model.Curriculum;
import jan.ondra.learnservice.curriculum.model.CurriculumStatus;
import jan.ondra.learnservice.curriculum.model.LearningUnit;
import jan.ondra.learnservice.curriculum.model.StudyContext;
import jan.ondra.learnservice.helper.DatabaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Import(CurriculumRepository.class)
@SuppressWarnings("AssertBetweenInconvertibleTypes")
class CurriculumRepositoryTest extends DatabaseIntegrationTest {

    @Autowired
    private CurriculumRepository curriculumRepository;

    @BeforeEach
    void setUp() throws IOException {
        //GIVEN
        var populateDbSql = new ClassPathResource("test-data/populate-db.sql").getContentAsString(UTF_8);
        jdbcTemplate.update(populateDbSql);
    }

    @Nested
    class PersistCurriculum {

        @Test
        @DisplayName("correctly persists given curriculum")
        void test1() {
            //GIVEN
            var userId = UUID.fromString("c63a90d1-6cc2-48fd-b889-1d3cb50b2b56");
            var createCurriculum = new CreateCurriculum(CurriculumStatus.ACTIVE, "Java", 1);

            //WHEN
            var curriculumId = curriculumRepository.persistCurriculum(userId, createCurriculum);

            //THEN
            var insertedCurriculum = selectCurriculum(curriculumId);
            assertThat(insertedCurriculum)
                .usingRecursiveComparison()
                .ignoringFields("id", "userId")
                .isEqualTo(createCurriculum);
            assertThat(insertedCurriculum.userId())
                .isEqualTo(userId);
        }

    }

    @Nested
    class PersistLearningUnits {

        @Test
        @DisplayName("correctly persists given learning units")
        void test1() {
            //GIVEN
            var curriculumId = UUID.fromString("c3e2f890-4f3d-4b29-8a7e-5b21d9d2c47f");
            var learningUnits = List.of(
                new CreateLearningUnit(
                    1,
                    "Introduction to Java",
                    "Basics",
                    "Java is a pretty nice programming language and ..."
                ),
                new CreateLearningUnit(
                    2,
                    "Primitive Types",
                    "int, boolean, char, ...",
                    null
                ),
                new CreateLearningUnit(
                    3,
                    "String",
                    "An Array of Characters",
                    null
                )
            );

            //WHEN
            curriculumRepository.persistLearningUnits(curriculumId, learningUnits);

            //THEN
            var insertedLearningUnits = selectLearningUnitsByCurriculumId(curriculumId);
            assertThat(insertedLearningUnits)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields("id", "curriculumId")
                .isEqualTo(learningUnits);
            insertedLearningUnits.forEach(l -> assertThat(l.curriculumId()).isEqualTo(curriculumId));
        }

    }

    @Nested
    class GetStudyContexts {

        @Test
        @DisplayName("returns correct data")
        void test1() {
            //WHEN
            var contexts = curriculumRepository.getStudyContexts(
                OffsetDateTime.of(LocalDateTime.of(2025, 11, 1, 9, 0), UTC)
            );

            //THEN
            assertThat(contexts).containsExactlyInAnyOrder(
                new StudyContext(
                    "david@example.com",
                    "de",
                    UUID.fromString("d02c9832-8b3e-4cc7-84a1-5ebeb9c13b12"),
                    "Cloud Computing",
                    "Virtual machines, containers, object/block storage.",
                    UUID.fromString("ab1234d5-e678-40a1-78bc-3c4d5e6f7081"),
                    "Networking in Cloud",
                    "VPCs"
                ),
                new StudyContext(
                    "emma@example.com",
                    "en",
                    UUID.fromString("e08da6a4-6bce-4a0f-b0cc-b52b8ab39b89"),
                    "Cybersecurity Basics",
                    "Personal and enterprise security tips.",
                    null,
                    null,
                    null
                )
            );
        }

    }

    @Nested
    class SetStatusToFinished {

        @Test
        @DisplayName("sets status of all mentioned curriculums to FINISHED")
        void test1() {
            //GIVEN
            var curriculumIds = List.of(
                UUID.fromString("a1f5033a-4b4d-47cd-90ce-d0b3df73af21"),
                UUID.fromString("b21b8e5e-733d-41d8-9062-8900563f2d39"),
                UUID.fromString("c3e2f890-4f3d-4b29-8a7e-5b21d9d2c47f")
            );

            //WHEN
            curriculumRepository.setStatusToFinished(curriculumIds);

            //THEN
            curriculumIds.forEach(id ->
                assertThat(selectCurriculum(id).status())
                    .isEqualTo(CurriculumStatus.FINISHED)
            );
        }

        @Test
        @DisplayName("doesn't fail when list is empty")
        void test2() {
            curriculumRepository.setStatusToFinished(List.of());
        }

    }

    @Nested
    class IncrementCurrentUnitNumber {

        @Test
        @DisplayName("increments current_unit_number of given curriculums")
        void test1() {
            //GIVEN
            var curriculumIds = List.of(
                UUID.fromString("a1f5033a-4b4d-47cd-90ce-d0b3df73af21"),
                UUID.fromString("b21b8e5e-733d-41d8-9062-8900563f2d39"),
                UUID.fromString("c3e2f890-4f3d-4b29-8a7e-5b21d9d2c47f")
            );

            //WHEN
            curriculumRepository.incrementCurrentUnitNumber(curriculumIds);

            //THEN
            assertThat(selectCurriculum(curriculumIds.getFirst()).currentUnitNumber()).isEqualTo(3);
            assertThat(selectCurriculum(curriculumIds.get(1)).currentUnitNumber()).isEqualTo(2);
            assertThat(selectCurriculum(curriculumIds.get(2)).currentUnitNumber()).isEqualTo(2);
        }

        @Test
        @DisplayName("doesn't fail when list is empty")
        void test2() {
            curriculumRepository.setStatusToFinished(List.of());
        }

    }

    @Nested
    class UpdateLearningUnitContents {

        @Test
        @DisplayName("updates the content of the given learning units")
        void test1() {
            //GIVEN
            var updates = Map.of(
                UUID.fromString("f303c418-928f-48e5-b9d3-893681e3ff03"), "content 1",
                UUID.fromString("fa21dc26-1902-4f2d-b112-be0129f1ff11"), "content 2",
                UUID.fromString("ab1234d5-e678-40a1-78bc-3c4d5e6f7081"), "content 3"
            );

            //WHEN
            curriculumRepository.updateLearningUnitContents(updates);

            //THEN
            assertThat(selectLearningUnit(UUID.fromString("f303c418-928f-48e5-b9d3-893681e3ff03")).content())
                .isEqualTo("content 1");
            assertThat(selectLearningUnit(UUID.fromString("fa21dc26-1902-4f2d-b112-be0129f1ff11")).content())
                .isEqualTo("content 2");
            assertThat(selectLearningUnit(UUID.fromString("ab1234d5-e678-40a1-78bc-3c4d5e6f7081")).content())
                .isEqualTo("content 3");
        }

        @Test
        @DisplayName("doesn't fail when map is empty")
        void test2() {
            curriculumRepository.updateLearningUnitContents(Map.of());
        }

    }

    private Curriculum selectCurriculum(UUID id) {
        return namedParameterJdbcTemplate.queryForObject(
            "SELECT * FROM curriculums WHERE id = :id",
            Map.of("id", id),
            new CurriculumRowMapper()
        );
    }

    private LearningUnit selectLearningUnit(UUID learningUnitId) {
        return namedParameterJdbcTemplate.queryForObject(
            "SELECT * FROM learning_units WHERE id = :learningUnitId",
            Map.of("learningUnitId", learningUnitId),
            new LearningUnitRowMapper()
        );
    }

    private List<LearningUnit> selectLearningUnitsByCurriculumId(UUID curriculumId) {
        return namedParameterJdbcTemplate.query(
            "SELECT * FROM learning_units WHERE curriculum_id = :curriculumId",
            Map.of("curriculumId", curriculumId),
            new LearningUnitRowMapper()
        );
    }

}
