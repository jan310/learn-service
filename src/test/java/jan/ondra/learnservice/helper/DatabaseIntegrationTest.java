package jan.ondra.learnservice.helper;

import jan.ondra.learnservice.domain.user.model.User;
import jan.ondra.learnservice.domain.user.persistence.UserRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@JdbcTest
public class DatabaseIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18beta3");

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM users; DELETE FROM curriculums;", Map.of());
    }

    public User buildUser(User user, UUID id) {
        return new User(
            id,
            user.authId(),
            user.notificationEnabled(),
            user.notificationEmail(),
            user.notificationTime(),
            user.timezone(),
            user.language()
        );
    }

    public User insertUser (User user) {
        var sql = """
            INSERT INTO users (
                auth_id,
                notification_enabled,
                notification_email,
                notification_time,
                time_zone,
                language
            )
            VALUES (
                :authId,
                :notificationEnabled,
                :notificationEmail,
                :notificationTime,
                :timeZone,
                :language
            );
            """;

        var paramSource = new MapSqlParameterSource()
            .addValue("authId", user.authId())
            .addValue("notificationEnabled", user.notificationEnabled())
            .addValue("notificationEmail", user.notificationEmail())
            .addValue("notificationTime", user.notificationTime())
            .addValue("timeZone", user.timezone())
            .addValue("language", user.language());

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"id"});

        return buildUser(user, keyHolder.getKeyAs(UUID.class));
    }

    public List<User> selectAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
    }

}
