package jan.ondra.learnservice.domain.user.persistence;

import jan.ondra.learnservice.domain.user.model.User;
import jan.ondra.learnservice.helper.DatabaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalTime;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@Import(UserRepository.class)
class UserRepositoryTest extends DatabaseIntegrationTest {

    @MockitoBean
    private Cache userCache;

    @Autowired
    private UserRepository userRepository;

    @Nested
    class PersistUser {

        @Test
        @DisplayName("creates user and caches user ID")
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

            var id = userRepository.persistUser(user);

            assertThat(selectAllUsers()).containsExactly(buildUser(user, id));

            verify(userCache).put("authId-1", id);
        }

        @Test
        @DisplayName("throws EmailAlreadyInUseException and does not use cache when email is already taken")
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
                .persistUser(
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

            verifyNoInteractions(userCache);
        }

    }

    @Nested
    class GetUserIdByAuthId {

        @Test
        @DisplayName("returns correct user ID when user ID was cached")
        void test1() {
            var id = UUID.randomUUID();

            when(userCache.get("authId-1", UUID.class)).thenReturn(id);

            assertThat(userRepository.getUserIdByAuthId("authId-1")).isEqualTo(id);
        }

        @Test
        @DisplayName("returns correct user ID when user ID was not cached")
        void test2() {
            when(userCache.get("authId-1", UUID.class)).thenReturn(null);

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

            assertThat(userRepository.getUserIdByAuthId("authId-1")).isEqualTo(id);
        }

    }

    @Nested
    class GetUser {

        @Test
        @DisplayName("returns correct user and caches its ID")
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

            verify(userCache).put("authId-1", user.id());
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
        @DisplayName("deletes correct user and removes its ID from cache")
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

            verify(userCache).evict("authId-2");
        }

    }

}
