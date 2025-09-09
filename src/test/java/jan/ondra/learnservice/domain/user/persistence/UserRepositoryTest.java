package jan.ondra.learnservice.domain.user.persistence;

import jan.ondra.learnservice.domain.user.model.User;
import jan.ondra.learnservice.helper.DatabaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(UserRepository.class)
class UserRepositoryTest extends DatabaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Nested
    class CreateUser {

        @Test
        @DisplayName("creates user")
        void test1() {
            var user = new User(
                null,
                "authId-1",
                true,
                "user1@email.com",
                LocalTime.now().truncatedTo(MINUTES),
                "Europe/Berlin",
                "de"
            );

            var id = userRepository.createUser(user);

            assertThat(selectAllUsers()).containsExactly(buildUser(user, id));
        }

        @Test
        @DisplayName("throws EmailAlreadyInUseException when email is already taken")
        void test2() {
            insertUser(
                new User(
                    null,
                    "authId-1",
                    true,
                    "user1@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            );

            assertThatThrownBy(() -> userRepository
                .createUser(
                    new User(
                        null,
                        "authId-2",
                        true,
                        "user1@email.com",
                        LocalTime.now().truncatedTo(MINUTES),
                        "Europe/Berlin",
                        "de"
                    )
                )
            ).isInstanceOf(EmailAlreadyInUseException.class);
        }

    }

    @Nested
    class GetIdByAuthId {

        @Test
        @DisplayName("returns correct user ID")
        void test1() {
            var id = insertUser(
                new User(
                    null,
                    "authId-1",
                    true,
                    "user1@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            ).id();

            insertUser(
                new User(
                    null,
                    "authId-2",
                    true,
                    "user2@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            );

            assertThat(userRepository.getIdByAuthId("authId-1")).isEqualTo(id);
        }

    }

    @Nested
    class GetUser {

        @Test
        @DisplayName("returns correct user")
        void test1() {
            var user = insertUser(
                new User(
                    null,
                    "authId-1",
                    true,
                    "user1@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            );

            insertUser(
                new User(
                    null,
                    "authId-2",
                    true,
                    "user2@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            );

            assertThat(userRepository.getUser("authId-1")).isEqualTo(user);
        }

    }

    @Nested
    class UpdateUser {

        @Test
        @DisplayName("updates user")
        void test1() {
            var id = insertUser(
                new User(
                    null,
                    "authId-1",
                    true,
                    "user1@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            ).id();

            var user = new User(
                null,
                "authId-1",
                false,
                "user-1@email.com",
                LocalTime.now().plusMinutes(30).truncatedTo(MINUTES),
                "Atlantic/Reykjavik",
                "en-us"
            );

            userRepository.updateUser(user);

            assertThat(selectAllUsers()).containsExactly(buildUser(user, id));
        }

        @Test
        @DisplayName("throws EmailAlreadyInUseException when email is already taken")
        void test2() {
            insertUser(
                new User(
                    null,
                    "authId-1",
                    true,
                    "user1@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            );

            insertUser(
                new User(
                    null,
                    "authId-2",
                    true,
                    "user2@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            );

            assertThatThrownBy(() -> userRepository
                .updateUser(
                    new User(
                        null,
                        "authId-1",
                        true,
                        "user2@email.com",
                        LocalTime.now().truncatedTo(MINUTES),
                        "Europe/Berlin",
                        "de"
                    )
                )
            ).isInstanceOf(EmailAlreadyInUseException.class);
        }

    }

    @Nested
    class DeleteUser {

        @Test
        @DisplayName("deletes correct user")
        void test1() {
            var user = insertUser(
                new User(
                    null,
                    "authId-1",
                    true,
                    "user1@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            );

            insertUser(
                new User(
                    null,
                    "authId-2",
                    true,
                    "user2@email.com",
                    LocalTime.now().truncatedTo(MINUTES),
                    "Europe/Berlin",
                    "de"
                )
            );

            userRepository.deleteUser("authId-2");

            assertThat(selectAllUsers()).containsExactly(user);
        }

    }

}