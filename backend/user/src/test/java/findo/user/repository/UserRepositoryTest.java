package findo.user.repository;

import findo.user.entity.EntityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // Use a test profile if you have one
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private EntityUser user;

    @BeforeEach
    void setUp() {
        user = new EntityUser();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole("ROLE_CUSTOMER");
        user.setBalance(100.0);
        user.setCreatedTime(LocalDate.now());
        user.setUpdatedTime(LocalDate.now());
        user.setCreatedBy("admin");
        user.setUpdatedBy("admin");

        userRepository.save(user);
    }

    @Test
    void testFindByEmail_ExistingEmail_ReturnsUser() {
        Optional<EntityUser> foundUser = userRepository.findByEmail("john.doe@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindByEmail_NonExistingEmail_ReturnsEmpty() {
        Optional<EntityUser> foundUser = userRepository.findByEmail("non.existing@example.com");

        assertThat(foundUser).isNotPresent();
    }
}