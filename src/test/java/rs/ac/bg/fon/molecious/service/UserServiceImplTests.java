package rs.ac.bg.fon.molecious.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import rs.ac.bg.fon.molecious.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.molecious.exception.UserDoesNotExistException;
import rs.ac.bg.fon.molecious.model.User;
import rs.ac.bg.fon.molecious.repository.UserRepository;
import rs.ac.bg.fon.molecious.service.impl.UserServiceImpl;

import java.util.Optional;

@SpringBootTest
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void signUpWhenUserDoesNotExistShouldCreateUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");

        Mockito.when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(user);

        Mockito.when(bCryptPasswordEncoder.encode("test"))
                .thenReturn("hashedTest");

        User savedUser = userService.signUp(user);
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void signUpWhenUserExistsShouldThrowUserAlreadyExistsException() {
        User user = new User();
        user.setEmail("test@test.com");

        Mockito.when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            userService.signUp(user);
        });

        String expectedMessage = "User with email test@test.com already exists.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void findByEmailWhenUserExistsShouldReturnUser() {
        User repoUser = new User();
        repoUser.setEmail("test@test.com");

        Mockito.when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(repoUser));

        User serviceUser = userService.findByEmail("test@test.com");
        Assertions.assertThat(repoUser.getEmail()).isEqualTo(serviceUser.getEmail());
    }

    @Test
    public void findByEmailWhenUserDoesNotExistShouldThrowUserDoesNotExistException() {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(UserDoesNotExistException.class, () -> {
            userService.findByEmail("test@test.com");
        });

        String expectedMessage = "User with email test@test.com doesn't exist.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
