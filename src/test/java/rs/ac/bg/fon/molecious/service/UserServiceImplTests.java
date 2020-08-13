package rs.ac.bg.fon.molecious.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import rs.ac.bg.fon.molecious.config.security.util.JwtUtil;
import rs.ac.bg.fon.molecious.exception.InvalidJWTException;
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
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void signUpWhenUserDoesNotExistShouldCreateUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(user);

        Mockito.when(bCryptPasswordEncoder.encode(user.getPassword()))
                .thenReturn("hashedTest");

        User savedUser = userService.signUp(user);
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void signUpWhenUserExistsShouldThrowUserAlreadyExistsException() {
        User user = new User();
        user.setEmail("test@test.com");

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            userService.signUp(user);
        });

        String expectedMessage = "User with email test@test.com already exists.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void extractUserFromJWTWhenUserExistsShouldReturnUser() {
        String jwt = "testJWT";
        User expectedUser = new User();
        expectedUser.setEmail("test@test.com");

        Mockito.when(jwtUtil.extractUsername(jwt))
                .thenReturn(expectedUser.getEmail());
        Mockito.when(userRepository.findByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));

        User actualUser = userService.extractUserFromJWT(jwt);
        Assertions.assertThat(expectedUser.getEmail()).isEqualTo(actualUser.getEmail());
    }

    @Test
    public void extractUserFromJWTWhenUserDoesNotExistShouldThrowUserDoesNotExistException() {
        String jwt = "testJWT";
        User expectedUser = new User();
        expectedUser.setEmail("test@test.com");

        Mockito.when(jwtUtil.extractUsername(jwt))
                .thenReturn(expectedUser.getEmail());
        Mockito.when(userRepository.findByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.empty());

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(UserDoesNotExistException.class, () -> {
            userService.extractUserFromJWT(jwt);
        });

        String expectedMessage = "User with email test@test.com doesn't exist.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void extractUserFromJWTWhenJWTIsInvalidShouldThrowInvalidJWTException() {
        String jwt = "wrongTestJWT";
        User expectedUser = new User();
        expectedUser.setEmail("test@test.com");

        Mockito.when(jwtUtil.extractUsername(jwt))
                .thenReturn(null);

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(InvalidJWTException.class, () -> {
            userService.extractUserFromJWT(jwt);
        });

        String expectedMessage = "Invalid JWT.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
