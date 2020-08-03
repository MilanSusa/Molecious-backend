package rs.ac.bg.fon.molecious.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rs.ac.bg.fon.molecious.model.User;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmailWhenUserDoesNotExistShouldNotReturnUser() {
        Optional<User> optionalUser = userRepository.findByEmail("test@example.com");
        Assertions.assertThat(optionalUser.isEmpty()).isEqualTo(true);
    }

    @Test
    public void findByEmailWhenEmailIsCorrectShouldReturnUser() {
        User user = new User();
        user.setEmail("test@example.com");
        testEntityManager.persist(user);

        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        Assertions.assertThat(optionalUser.isPresent()).isEqualTo(true);
    }

    @Test
    public void findByEmailWhenEmailIsNotCorrectShouldNotReturnUser() {
        User user = new User();
        user.setEmail("test@example.com");
        testEntityManager.persist(user);

        Optional<User> optionalUser = userRepository.findByEmail("wrong.test@example.com");
        Assertions.assertThat(optionalUser.isEmpty()).isEqualTo(true);
    }
}
