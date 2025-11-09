package jan.ondra.learnservice.curriculum.repository;

import jan.ondra.learnservice.curriculum.model.StudyContext;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class StudyContextRowMapper implements RowMapper<StudyContext> {

    @Override
    public StudyContext mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StudyContext(
            rs.getObject("user_id", UUID.class),
            rs.getString("notification_email"),
            rs.getString("language"),
            rs.getObject("curriculum_id", UUID.class),
            rs.getString("topic"),
            rs.getString("current_content"),
            rs.getObject("next_unit_id", UUID.class),
            rs.getString("next_heading"),
            rs.getString("next_subheading")
        );
    }

}
