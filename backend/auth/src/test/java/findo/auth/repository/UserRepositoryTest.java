package findo.auth.repository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import findo.auth.data.entity.User;
import findo.auth.data.repository.UserRepository;

@DataJpaTest
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail_Success() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        user.setRole("ROLE_CUSTOMER");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("johndoe@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("johndoe@example.com");
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertThat(foundUser).isNotPresent();
    }
}
