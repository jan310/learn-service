package jan.ondra.learnservice.domain.curriculum.persistence;

import jan.ondra.learnservice.domain.curriculum.model.CreateCurriculum;
import jan.ondra.learnservice.domain.curriculum.model.CreateLearningUnit;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CurriculumRepository {

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
                number_of_units,
                current_unit_number
            )
            VALUES (
                :userId,
                :status,
                :topic,
                :numberOfUnits,
                :currentUnitNumber
            )
            RETURNING id;
            """;

        var paramSource = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("status", createCurriculum.status().name())
            .addValue("topic", createCurriculum.topic())
            .addValue("numberOfUnits", createCurriculum.numberOfUnits())
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

}
