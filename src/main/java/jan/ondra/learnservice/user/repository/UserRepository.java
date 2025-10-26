package jan.ondra.learnservice.user.repository;

import jan.ondra.learnservice.user.model.CreateUser;
import jan.ondra.learnservice.user.model.ModifyUser;
import jan.ondra.learnservice.user.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private static final String USERS_NOTIFICATION_EMAIL_UNIQUE_VIOLATION =
        "violates unique constraint \"uq_users_notification_email\"";
    private static final String USERS_CACHE_NAME = "users";

    private static final UserRowMapper userRowMapper = new UserRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @CachePut(value = USERS_CACHE_NAME, key = "#createUser.authSubject()")
    public User persistUser(CreateUser createUser) {
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

        try {
            return jdbcTemplate.queryForObject(sql, paramSource, userRowMapper);
        } catch (DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getMessage().contains(USERS_NOTIFICATION_EMAIL_UNIQUE_VIOLATION)) {
                throw new EmailAlreadyInUseException(e);
            } else {
                throw e;
            }
        }
    }

    @Cacheable(value = USERS_CACHE_NAME, key = "#authSubject")
    public User getUser(String authSubject) {
        var sql = "SELECT * FROM users WHERE auth_subject = :authSubject";
        var paramSource = new MapSqlParameterSource("authSubject", authSubject);
        return jdbcTemplate.queryForObject(sql, paramSource, userRowMapper);
    }

    @CachePut(value = USERS_CACHE_NAME, key = "#modifyUser.authSubject()")
    public User updateUser(ModifyUser modifyUser) {
        var sql = """
            UPDATE users SET
                notification_enabled = :notificationEnabled,
                notification_email = :notificationEmail,
                notification_time = :notificationTime,
                time_zone = :timeZone,
                language = :language
            WHERE auth_subject = :authSubject
            RETURNING *;
            """;

        var paramSource = new MapSqlParameterSource()
            .addValue("authSubject", modifyUser.authSubject())
            .addValue("notificationEnabled", modifyUser.notificationEnabled())
            .addValue("notificationEmail", modifyUser.notificationEmail())
            .addValue("notificationTime", modifyUser.notificationTime())
            .addValue("timeZone", modifyUser.timeZone())
            .addValue("language", modifyUser.language());

        try {
            return jdbcTemplate.queryForObject(sql, paramSource, userRowMapper);
        } catch (DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getMessage().contains(USERS_NOTIFICATION_EMAIL_UNIQUE_VIOLATION)) {
                throw new EmailAlreadyInUseException(e);
            } else {
                throw e;
            }
        }
    }

    @CacheEvict(value = USERS_CACHE_NAME, key = "#authSubject")
    public void deleteUser(String authSubject) {
        var sql = "DELETE FROM users WHERE auth_subject = :authSubject;";
        var paramSource = new MapSqlParameterSource("authSubject", authSubject);
        jdbcTemplate.update(sql, paramSource);
    }

}
