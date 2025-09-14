package jan.ondra.learnservice.domain.user.persistence;

import jan.ondra.learnservice.domain.user.model.User;
import org.springframework.cache.Cache;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRepository {

    private static final String USERS_NOTIFICATION_EMAIL_UNIQUE_VIOLATION =
        "violates unique constraint \"uq_users_notification_email\"";

    private static final UserRowMapper userRowMapper = new UserRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Cache userCache;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate, Cache userCache) {
        this.jdbcTemplate = jdbcTemplate;
        this.userCache = userCache;
    }

    public UUID persistUser(User user) {
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

        try {
            jdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"id"});
        } catch (DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getMessage().contains(USERS_NOTIFICATION_EMAIL_UNIQUE_VIOLATION)) {
                throw new EmailAlreadyInUseException(e);
            } else {
                throw e;
            }
        }

        var userId = keyHolder.getKeyAs(UUID.class);
        userCache.put(user.authId(), userId);
        return userId;
    }

    public UUID getUserIdByAuthId(String authId) {
        var cachedUserId = userCache.get(authId, UUID.class);
        if (cachedUserId != null) {
            return cachedUserId;
        } else {
            var sql = "SELECT id FROM users WHERE auth_id = :authId";
            var paramSource = new MapSqlParameterSource("authId", authId);
            var userId = jdbcTemplate.queryForObject(sql, paramSource, UUID.class);
            userCache.put(authId, userId);
            return userId;
        }
    }

    public User getUser(String authId) {
        var sql = "SELECT * FROM users WHERE auth_id = :authId";
        var paramSource = new MapSqlParameterSource("authId", authId);
        var user = jdbcTemplate.queryForObject(sql, paramSource, userRowMapper);
        userCache.put(authId, user.id());
        return user;
    }

    public void updateUser(User user) {
        var sql = """
            UPDATE users SET
                notification_enabled = :notificationEnabled,
                notification_email = :notificationEmail,
                notification_time = :notificationTime,
                time_zone = :timeZone,
                language = :language
            WHERE auth_id = :authId;
            """;

        var paramSource = new MapSqlParameterSource()
            .addValue("authId", user.authId())
            .addValue("notificationEnabled", user.notificationEnabled())
            .addValue("notificationEmail", user.notificationEmail())
            .addValue("notificationTime", user.notificationTime())
            .addValue("timeZone", user.timezone())
            .addValue("language", user.language());

        try {
            jdbcTemplate.update(sql, paramSource);
        } catch (DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getMessage().contains(USERS_NOTIFICATION_EMAIL_UNIQUE_VIOLATION)) {
                throw new EmailAlreadyInUseException(e);
            } else {
                throw e;
            }
        }
    }

    public void deleteUser(String authId) {
        var sql = "DELETE FROM users WHERE auth_id = :authId;";
        var paramSource = new MapSqlParameterSource("authId", authId);
        jdbcTemplate.update(sql, paramSource);
        userCache.evict(authId);
    }

}
