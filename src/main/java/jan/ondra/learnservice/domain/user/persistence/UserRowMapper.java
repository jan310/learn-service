package jan.ondra.learnservice.domain.user.persistence;

import jan.ondra.learnservice.domain.user.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new User(
            rs.getObject("id", UUID.class),
            rs.getString("auth_id"),
            rs.getBoolean("notification_enabled"),
            rs.getString("notification_email"),
            rs.getTime("notification_time").toLocalTime(),
            rs.getString("time_zone"),
            rs.getString("language")
        );
    }

}
