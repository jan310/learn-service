package jan.ondra.learnservice.curriculum.repository;

import jan.ondra.learnservice.curriculum.model.Curriculum;
import jan.ondra.learnservice.curriculum.model.CurriculumStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CurriculumRowMapper implements RowMapper<Curriculum> {

    @Override
    public Curriculum mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Curriculum(
            rs.getObject("id", UUID.class),
            rs.getObject("user_id", UUID.class),
            CurriculumStatus.valueOf(rs.getString("status")),
            rs.getString("topic"),
            rs.getInt("current_unit_number")
        );
    }

}
