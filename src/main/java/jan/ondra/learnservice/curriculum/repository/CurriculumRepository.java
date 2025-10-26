package jan.ondra.learnservice.curriculum.repository;

import jan.ondra.learnservice.curriculum.model.CreateCurriculum;
import jan.ondra.learnservice.curriculum.model.CreateLearningUnit;
import jan.ondra.learnservice.curriculum.model.StudyContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class CurriculumRepository {

    private static final StudyContextRowMapper studyContextRowMapper = new StudyContextRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CurriculumRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID persistCurriculum(UUID userId, CreateCurriculum createCurriculum) {
        var sql = """
            INSERT INTO curriculums (
                user_id,
                status,
                topic,
                current_unit_number
            )
            VALUES (
                :userId,
                :status,
                :topic,
                :currentUnitNumber
            )
            RETURNING id;
            """;

        var paramSource = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("status", createCurriculum.status().name())
            .addValue("topic", createCurriculum.topic())
            .addValue("currentUnitNumber", createCurriculum.currentUnitNumber());

        return jdbcTemplate.queryForObject(sql, paramSource, UUID.class);
    }

    public void persistLearningUnits(UUID curriculumId, List<CreateLearningUnit> createLearningUnits) {
        var sql = """
            INSERT INTO learning_units (
                curriculum_id,
                number,
                heading,
                subheading,
                content
            )
            VALUES (
                :curriculumId,
                :number,
                :heading,
                :subheading,
                :content
            );
            """;

        var batchValues = createLearningUnits.stream()
            .map(emptyLearningUnit -> new MapSqlParameterSource()
                .addValue("curriculumId", curriculumId)
                .addValue("number", emptyLearningUnit.number())
                .addValue("heading", emptyLearningUnit.heading())
                .addValue("subheading", emptyLearningUnit.subheading())
                .addValue("content", emptyLearningUnit.content())
            )
            .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, batchValues);
    }

    public List<StudyContext> getStudyContexts(OffsetDateTime utcDateTime) {
        var sql = """
            SELECT
                u.notification_email,
                u.language,
                c.id AS curriculum_id,
                c.topic,
                c.current_unit_number,
                cu.content AS current_content,
                nu.id AS next_unit_id,
                nu.heading AS next_heading,
                nu.subheading AS next_subheading
            FROM users u
            INNER JOIN curriculums c
                ON c.user_id = u.id
                AND c.status = 'ACTIVE'
            INNER JOIN learning_units cu
                ON cu.curriculum_id = c.id
                AND cu.number = c.current_unit_number
            LEFT JOIN learning_units nu
                ON nu.curriculum_id = c.id
                AND nu.number = c.current_unit_number + 1
            WHERE
                u.notification_enabled = true
                AND u.notification_time = (:utcDateTime AT TIME ZONE u.time_zone)::time;
            """;

        var paramSource = new MapSqlParameterSource("utcDateTime", utcDateTime);

        return jdbcTemplate.query(sql, paramSource, studyContextRowMapper);
    }

    public void setStatusToFinished(List<UUID> curriculumIds) {
        if (curriculumIds.isEmpty()) {
            return;
        }

        var sql = """
            UPDATE curriculums
            SET status = 'FINISHED'
            WHERE id IN (:curriculumIds);
            """;

        var paramSource = new MapSqlParameterSource("curriculumIds", curriculumIds);

        jdbcTemplate.update(sql, paramSource);
    }

    public void incrementCurrentUnitNumber(List<UUID> curriculumIds) {
        if (curriculumIds.isEmpty()) {
            return;
        }

        var sql = """
            UPDATE curriculums
            SET current_unit_number = current_unit_number + 1
            WHERE id IN (:curriculumIds);
            """;

        var paramSource = new MapSqlParameterSource("curriculumIds", curriculumIds);

        jdbcTemplate.update(sql, paramSource);
    }

    public void updateLearningUnitContents(Map<UUID, String> learningUnitContents) {
        if (learningUnitContents.isEmpty()) {
            return;
        }

        var sql = """
            UPDATE learning_units
            SET content = :content
            WHERE id = :id
            """;

        var batchValues = learningUnitContents.entrySet().stream()
            .map(entry -> new MapSqlParameterSource()
                .addValue("id", entry.getKey())
                .addValue("content", entry.getValue())
            )
            .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, batchValues);
    }

}
