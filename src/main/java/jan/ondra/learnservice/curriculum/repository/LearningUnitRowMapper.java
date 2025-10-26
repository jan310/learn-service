package jan.ondra.learnservice.curriculum.repository;

import jan.ondra.learnservice.curriculum.model.LearningUnit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LearningUnitRowMapper implements RowMapper<LearningUnit> {

    @Override
    public LearningUnit mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new LearningUnit(
            rs.getObject("id", UUID.class),
            rs.getObject("curriculum_id", UUID.class),
            rs.getInt("number"),
            rs.getString("heading"),
            rs.getString("subheading"),
            rs.getString("content")
        );
    }

}
