package jan.ondra.learnservice.domain.curriculum.persistence;

import jan.ondra.learnservice.domain.curriculum.model.Curriculum;
import jan.ondra.learnservice.domain.curriculum.model.EmptyLearningUnit;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CurriculumRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CurriculumRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID persistCurriculum(Curriculum curriculum) {
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
            );
            """;

        var paramSource = new MapSqlParameterSource()
            .addValue("userId", curriculum.userId())
            .addValue("status", curriculum.status().name())
            .addValue("topic", curriculum.topic())
            .addValue("numberOfUnits", curriculum.numberOfUnits())
            .addValue("currentUnitNumber", curriculum.currentUnitNumber());

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"id"});

        return keyHolder.getKeyAs(UUID.class);
    }

    public void persistEmptyLearningUnits(UUID curriculumId, List<EmptyLearningUnit> emptyLearningUnits) {
        var sql = """
            INSERT INTO learning_units (curriculum_id, number, heading, subheading)
            VALUES (:curriculumId, :number, :heading, :subheading);
            """;

        var batchValues = emptyLearningUnits.stream()
            .map(emptyLearningUnit -> new MapSqlParameterSource()
                .addValue("curriculumId", curriculumId)
                .addValue("number", emptyLearningUnit.number())
                .addValue("heading", emptyLearningUnit.heading())
                .addValue("subheading", emptyLearningUnit.subheading())
            )
            .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, batchValues);
    }

}
