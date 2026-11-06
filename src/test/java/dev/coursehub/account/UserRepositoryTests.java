package dev.coursehub.account;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTests {

    @Autowired
    private UserRepository users;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savesAndReadsUserByUsername() {
        User saved = users.saveAndFlush(
                new User("avery", "Avery Lane", "avery@example.com", "test-hash", Role.LEARNER)
        );
        Long id = saved.getId();
        assertThat(id).isNotNull();

        entityManager.clear();

        User loaded = users.findByUsername("avery").orElseThrow();
        assertThat(loaded.getId()).isEqualTo(id);
        assertThat(loaded.getUsername()).isEqualTo("avery");
        assertThat(loaded.getFullName()).isEqualTo("Avery Lane");
        assertThat(loaded.getEmail()).isEqualTo("avery@example.com");
        assertThat(loaded.getPasswordHash()).isEqualTo("test-hash");
        assertThat(loaded.getRole()).isEqualTo(Role.LEARNER);
    }

    @Test
    void rejectsDuplicateUsername() {
        users.saveAndFlush(
                new User("avery", "Avery Lane", "avery@example.com", "test-hash", Role.LEARNER)
        );

        assertThatThrownBy(() -> users.saveAndFlush(
                new User("avery", "Morgan Reed", "morgan@example.com", "test-hash", Role.INSTRUCTOR)
        )).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rejectsDuplicateEmail() {
        users.saveAndFlush(
                new User("avery", "Avery Lane", "avery@example.com", "test-hash", Role.LEARNER)
        );

        assertThatThrownBy(() -> users.saveAndFlush(
                new User("morgan", "Morgan Reed", "avery@example.com", "test-hash", Role.INSTRUCTOR)
        )).isInstanceOf(DataIntegrityViolationException.class);
    }
}