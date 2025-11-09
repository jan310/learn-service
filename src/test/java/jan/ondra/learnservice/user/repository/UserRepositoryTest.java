package jan.ondra.learnservice.user.repository;

import jan.ondra.learnservice.helper.DatabaseIntegrationTest;
import jan.ondra.learnservice.user.model.CreateUser;
import jan.ondra.learnservice.user.model.ModifyUser;
import jan.ondra.learnservice.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Import(UserRepository.class)
class UserRepositoryTest extends DatabaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM users;");
    }

    @Nested
    class PersistUser {

        @Test
        @DisplayName("persists user")
        void test1() {
            //GIVEN
            var createUser = new CreateUser(
                "authSubject",
                true,
                "test@email.com",
                LocalTime.of(5,0),
                "Europe/Berlin",
                "de"
            );

            //WHEN
            var returnedUser = userRepository.persistUser(createUser);

            //THEN
            var persistedUsers = selectAllUsersFromDB();
            assertThat(persistedUsers.size())
                .isEqualTo(1);
            assertThat(persistedUsers.getFirst())
                .isEqualTo(returnedUser);
            assertThat(returnedUser)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(createUser);
        }

        @Test
        @DisplayName("throws EmailAlreadyInUseException when email is already used by another user")
        void test2() {
            //GIVEN
            var email = "test@email.com";
            inserUser(new CreateUser(
                "authSubject1",
                true,
                email,
                LocalTime.of(5,0),
                "Europe/Berlin",
                "de"
            ));
            var createUser = new CreateUser(
                "authSubject2",
                true,
                email,
                LocalTime.of(5,0),
                "Europe/Berlin",
                "de"
            );

            //WHEN-THEN
            assertThatThrownBy(() -> userRepository.persistUser(createUser))
                .isInstanceOf(EmailAlreadyInUseException.class);
        }

    }

    @Nested
    class GetUser {

        @Test
        @DisplayName("returns correct user")
        void test1() {
            //GIVEN
            var user1 = inserUser(new CreateUser(
                "authSubject1",
                true,
                "test1@email.com",
                LocalTime.of(5,0),
                "Europe/Berlin",
                "de"
            ));
            inserUser(new CreateUser(
                "authSubject2",
                true,
                "test2@email.com",
                LocalTime.of(5,0),
                "Europe/Berlin",
                "de"
            ));

            //WHEN
            var returnedUser = userRepository.getUser("authSubject1");

            //THEN
            assertThat(returnedUser).isEqualTo(user1);
        }

    }

    @Nested
    class UpdateUser {

        @Test
        @DisplayName("updates user")
        void test1() {
            //GIVEN
            inserUser(new CreateUser(
                "authSubject1",
                true,
                "test1@email.com",
                LocalTime.of(5,0),
                "Europe/Berlin",
                "de"
            ));
            var updateUser = new ModifyUser(
                "authSubject1",
                false,
                "test2@email.com",
                LocalTime.of(6,15),
                "Europe/Paris",
                "fr"
            );

            //WHEN
            var returnedUser = userRepository.updateUser(updateUser);

            //THEN
            var persistedUsers = selectAllUsersFromDB();
            assertThat(persistedUsers.getFirst())
                .isEqualTo(returnedUser);
            assertThat(returnedUser)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(updateUser);
        }

        @Test
        @DisplayName("throws EmailAlreadyInUseException when email is already used by another user")
        void test2() {
            //GIVEN
            inserUser(new CreateUser(
                "authSubject1",
                true,
                "test1@email.com",
                LocalTime.of(5,0),
                "Europe/Berlin",
                "de"
            ));
            inserUser(new CreateUser(
                "authSubject2",
                true,
                "test2@email.com",
                LocalTime.of(5,0),
                "Europe/Berlin",
                "de"
            ));
            var updateUser = new ModifyUser(
                "authSubject2",
                false,
                "test1@email.com",
                LocalTime.of(6,15),
                "Europe/Paris",
                "fr"
            );

            //WHEN-THEN
            assertThatThrownBy(() -> userRepository.updateUser(updateUser))
                .isInstanceOf(EmailAlreadyInUseException.class);
        }

    }

    @Nested
    class DeleteUser {

        @Test
        @DisplayName("deletes correct user")
        void test1() {
            //GIVEN
            var user1 = inserUser(new CreateUser(
                "authSubject1",
                true,
                "test1@email.com",
                LocalTime.of(5,0),
                "Europe/Berlin",
                "de"
            ));
            var user2 = inserUser(
                new CreateUser(
                    "authSubject2",
                    true,
                    "test2@email.com",
                    LocalTime.of(5,0),
                    "Europe/Berlin",
                    "de"
                )
            );

            //WHEN
            userRepository.deleteUser(user1.authSubject());

            //THEN
            var persistedUsers = selectAllUsersFromDB();
            assertThat(persistedUsers.size())
                .isEqualTo(1);
            assertThat(persistedUsers.getFirst())
                .isEqualTo(user2);
        }

    }

    private User inserUser(CreateUser createUser) {
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

        return namedParameterJdbcTemplate.queryForObject(sql, paramSource, new UserRowMapper());
    }

    private List<User> selectAllUsersFromDB() {
        return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
    }

}
