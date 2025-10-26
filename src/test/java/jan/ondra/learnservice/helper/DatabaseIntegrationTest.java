package jan.ondra.learnservice.helper;

import jan.ondra.learnservice.user.model.CreateUser;
import jan.ondra.learnservice.user.model.User;
import jan.ondra.learnservice.user.repository.UserRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.Map;

@JdbcTest
public class DatabaseIntegrationTest {

    private static final UserRowMapper userRowMapper = new UserRowMapper();

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18.0");

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM users;", Map.of());
    }

    public User inserUser(CreateUser createUser) {
        var sql = """
            INSERT INTO users (
                auth_subject,
                notification_enabled,
                notification_email,
                notification_time,
                time_zone,
                language
            )
            VALUES (
                :authSubject,
                :notificationEnabled,
                :notificationEmail,
                :notificationTime,
                :timeZone,
                :language
            )
            RETURNING *;
            """;

        var paramSource = new MapSqlParameterSource()
            .addValue("authSubject", createUser.authSubject())
            .addValue("notificationEnabled", createUser.notificationEnabled())
            .addValue("notificationEmail", createUser.notificationEmail())
            .addValue("notificationTime", createUser.notificationTime())
            .addValue("timeZone", createUser.timeZone())
            .addValue("language", createUser.language());

        return jdbcTemplate.queryForObject(sql, paramSource, userRowMapper);
    }

    public List<User> selectAllUsersFromDB() {
        return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
    }

}
