package jan.ondra.learnservice.curriculum.repository;

import jan.ondra.learnservice.curriculum.model.CreateCurriculum;
import jan.ondra.learnservice.curriculum.model.CreateLearningUnit;
import jan.ondra.learnservice.curriculum.model.Curriculum;
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

@Import(CurriculumRepository.class)
@SuppressWarnings("AssertBetweenInconvertibleTypes")
class CurriculumRepositoryTest extends DatabaseIntegrationTest {

    @Autowired
    private CurriculumRepository curriculumRepository;

    @BeforeEach
    void setUp() throws IOException {
        var populateDbSql = new ClassPathResource("test-data/populate-db.sql").getContentAsString(UTF_8);
        jdbcTemplate.update(populateDbSql);
    }

    @Nested
    class GetCurriculumQueueSizeForUser {

        @Test
        @DisplayName("returns the number of curriculums the user has in his queue")
        void test1() {
            //GIVEN
            var aliceId = UUID.fromString("018f1c9a-1111-4e7b-a9e1-12a7f6f01a10");

            //WHEN
            var result = curriculumRepository.getCurriculumQueueSizeForUser(aliceId);

            //THEN
            assertThat(result).isEqualTo(2);
        }

    }

    @Nested
    class PersistCurriculum {

        @Test
        @DisplayName("correctly persists given curriculum")
        void test1() {
            //GIVEN
            var aliceId = UUID.fromString("018f1c9a-1111-4e7b-a9e1-12a7f6f01a10");
            var createCurriculum = new CreateCurriculum(2, "Java", 1);

            //WHEN
            var curriculumId = curriculumRepository.persistCurriculum(aliceId, createCurriculum);

            //THEN
            var insertedCurriculum = selectCurriculum(curriculumId);
            assertThat(insertedCurriculum)
                .usingRecursiveComparison()
                .ignoringFields("id", "userId")
                .isEqualTo(createCurriculum);
            assertThat(insertedCurriculum.userId())
                .isEqualTo(aliceId);
        }

    }

    @Nested
    class PersistLearningUnits {

        @Test
        @DisplayName("correctly persists given learning units")
        void test1() {
            //GIVEN
            var curriculumId = UUID.fromString("018f1d00-2001-4f3a-9e11-aaa111111001");
            jdbcTemplate.update(
                "DELETE FROM learning_units WHERE curriculum_id = '018f1d00-2001-4f3a-9e11-aaa111111001'"
            );
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
            //GIVEN
            var utcDateTime = OffsetDateTime.of(LocalDateTime.of(2025, 11, 12, 5, 0), UTC);

            //WHEN
            var contexts = curriculumRepository.getStudyContexts(utcDateTime);

            //THEN
            assertThat(contexts).containsExactlyInAnyOrder(
                new StudyContext(
                    UUID.fromString("018f1c9a-1111-4e7b-a9e1-12a7f6f01a10"),
                    "alice@example.com",
                    "en",
                    UUID.fromString("018f1d00-2001-4f3a-9e11-aaa111111001"),
                    "Intro to Databases",
                    "A database is an organized collection of data that can be easily accessed, managed, and updated.",
                    UUID.fromString("018f1e00-3002-4a9e-9e11-bbb111111002"),
                    "Database Models",
                    "Relational vs Non-relational"
                ),
                new StudyContext(
                    UUID.fromString("018f1c9a-1112-4b6d-bc42-87f5c6b20a21"),
                    "bob@example.com",
                    "en",
                    UUID.fromString("018f1d00-2004-4f3a-9e11-aaa111111004"),
                    "Web Development",
                    "Overview of how websites work, from clients to servers.",
                    UUID.fromString("018f1e00-4002-4a9e-9e11-bbb111111002"),
                    "HTML & CSS Basics",
                    "Building web pages"
                )
            );
        }

    }

    @Nested
    class AdvanceCurriculumQueueForUsers {

        @Test
        @DisplayName("sets status of all mentioned curriculums to FINISHED")
        void test1() {
            //GIVEN
            var aliceId = UUID.fromString("018f1c9a-1111-4e7b-a9e1-12a7f6f01a10");
            var bobId = UUID.fromString("018f1c9a-1112-4b6d-bc42-87f5c6b20a21");
            var carolId = UUID.fromString("018f1c9a-1113-4c82-91f5-21c0e9d54a32");

            //WHEN
            curriculumRepository.advanceCurriculumQueueForUsers(List.of(aliceId, bobId, carolId));

            //THEN
            // Alice's curriculums
            assertThat(selectCurriculum(UUID.fromString("018f1d00-2001-4f3a-9e11-aaa111111001")).queuePosition()).isEqualTo(-1);
            assertThat(selectCurriculum(UUID.fromString("018f1d00-2002-4f3a-9e11-aaa111111002")).queuePosition()).isEqualTo(0);
            assertThat(selectCurriculum(UUID.fromString("018f1d00-2003-4f3a-9e11-aaa111111003")).queuePosition()).isEqualTo(-1);
            // Bobs's curriculums
            assertThat(selectCurriculum(UUID.fromString("018f1d00-2004-4f3a-9e11-aaa111111004")).queuePosition()).isEqualTo(-1);
            assertThat(selectCurriculum(UUID.fromString("018f1d00-2005-4f3a-9e11-aaa111111005")).queuePosition()).isEqualTo(0);
            assertThat(selectCurriculum(UUID.fromString("018f1d00-2006-4f3a-9e11-aaa111111006")).queuePosition()).isEqualTo(-1);
            // Carlos's curriculums
            assertThat(selectCurriculum(UUID.fromString("018f1d00-2007-4f3a-9e11-aaa111111007")).queuePosition()).isEqualTo(-1);
            assertThat(selectCurriculum(UUID.fromString("018f1d00-2008-4f3a-9e11-aaa111111008")).queuePosition()).isEqualTo(-1);
            assertThat(selectCurriculum(UUID.fromString("018f1d00-2009-4f3a-9e11-aaa111111009")).queuePosition()).isEqualTo(-1);
        }

        @Test
        @DisplayName("doesn't fail when list is empty")
        void test2() {
            curriculumRepository.advanceCurriculumQueueForUsers(List.of());
        }

    }

    @Nested
    class IncrementCurrentUnitNumber {

        @Test
        @DisplayName("increments current_unit_number of given curriculums")
        void test1() {
            //GIVEN
            var curriculum1 = UUID.fromString("018f1d00-2001-4f3a-9e11-aaa111111001");
            var curriculum2 = UUID.fromString("018f1d00-2004-4f3a-9e11-aaa111111004");

            //WHEN
            curriculumRepository.incrementCurrentUnitNumber(List.of(curriculum1, curriculum2));

            //THEN
            assertThat(selectCurriculum(curriculum1).currentUnitNumber()).isEqualTo(2);
            assertThat(selectCurriculum(curriculum2).currentUnitNumber()).isEqualTo(2);
        }

        @Test
        @DisplayName("doesn't fail when list is empty")
        void test2() {
            curriculumRepository.incrementCurrentUnitNumber(List.of());
        }

    }

    @Nested
    class UpdateLearningUnitContents {

        @Test
        @DisplayName("updates the content of the given learning units")
        void test1() {
            //GIVEN
            var unit1 = UUID.fromString("018f1e00-3001-4a9e-9e11-bbb111111001");
            var unit2 = UUID.fromString("018f1e00-3002-4a9e-9e11-bbb111111002");
            var unit3 = UUID.fromString("018f1e00-3003-4a9e-9e11-bbb111111003");

            //WHEN
            curriculumRepository.updateLearningUnitContents(
                Map.of(
                    unit1, "content 1",
                    unit2, "content 2",
                    unit3, "content 3"
                )
            );

            //THEN
            assertThat(selectLearningUnit(unit1).content())
                .isEqualTo("content 1");
            assertThat(selectLearningUnit(unit2).content())
                .isEqualTo("content 2");
            assertThat(selectLearningUnit(unit3).content())
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
