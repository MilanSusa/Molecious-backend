package rs.ac.bg.fon.molecious.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import rs.ac.bg.fon.molecious.config.security.util.JwtUtil;
import rs.ac.bg.fon.molecious.exception.InvalidJWTException;
import rs.ac.bg.fon.molecious.exception.UserDoesNotExistException;
import rs.ac.bg.fon.molecious.model.Inference;
import rs.ac.bg.fon.molecious.model.User;
import rs.ac.bg.fon.molecious.repository.InferenceRepository;
import rs.ac.bg.fon.molecious.repository.UserRepository;
import rs.ac.bg.fon.molecious.service.impl.InferenceServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class InferenceServiceImplTests {

    @Mock
    private InferenceRepository inferenceRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private InferenceServiceImpl inferenceService;

    @Test
    public void findAllByUserJWTWhenJWTIsNotValidShouldThrowInvalidJWTException() {
        User user = new User();
        user.setEmail("test@test.com");

        Mockito.when(jwtUtil.extractUsername("testJWT"))
                .thenReturn(user.getEmail());

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(InvalidJWTException.class, () -> {
            inferenceService.findAllByUserJWT("wrongTestJWT");
        });

        String expectedMessage = "Invalid JWT.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void findAllByUserJWTWhenUserDoesNotExistShouldThrowUserDoesNotExistException() {
        User user = new User();
        user.setId(1l);
        user.setEmail("test@test.com");

        String testJWT = "testJWT";

        Mockito.when(jwtUtil.extractUsername(testJWT))
                .thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(UserDoesNotExistException.class, () -> {
            inferenceService.findAllByUserJWT(testJWT);
        });

        String expectedMessage = "User with email test@test.com does not exist.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void findAllByUserJWTWhenInferencesForUserDoNotExistShouldNotReturnAnyInferences() {
        User user = new User();
        user.setId(1l);
        user.setEmail("test@test.com");

        List<Inference> expectedInferences = Collections.emptyList();
        String testJWT = "testJWT";

        Mockito.when(jwtUtil.extractUsername(testJWT))
                .thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Mockito.when(inferenceRepository.findAllByUserId(user.getId()))
                .thenReturn(expectedInferences);

        List<Inference> actualInferences = inferenceService.findAllByUserJWT(testJWT);

        Assertions.assertThat(actualInferences).isEqualTo(expectedInferences);
    }

    @Test
    public void findAllByUserJWTWhenJWTIsValidShouldReturnSingleInference() {
        User user = new User();
        user.setId(1l);
        user.setEmail("test@test.com");

        Inference inference = new Inference();
        inference.setUser(user);
        List<Inference> expectedInferences = List.of(inference);

        String testJWT = "testJWT";

        Mockito.when(jwtUtil.extractUsername(testJWT))
                .thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Mockito.when(inferenceRepository.findAllByUserId(user.getId()))
                .thenReturn(List.of(inference));

        List<Inference> actualInferences = inferenceService.findAllByUserJWT(testJWT);

        Assertions.assertThat(actualInferences).isEqualTo(expectedInferences);
    }

    @Test
    public void findAllByUserJWTWhenJWTIsValidShouldReturnMultipleInferences() {
        User user = new User();
        user.setId(1l);
        user.setEmail("test@test.com");

        Inference firstInference = new Inference();
        firstInference.setUser(user);
        Inference secondInference = new Inference();
        firstInference.setUser(user);
        List<Inference> expectedInferences = List.of(firstInference, secondInference);

        String testJWT = "testJWT";

        Mockito.when(jwtUtil.extractUsername(testJWT))
                .thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Mockito.when(inferenceRepository.findAllByUserId(user.getId()))
                .thenReturn(List.of(firstInference, secondInference));

        List<Inference> actualInferences = inferenceService.findAllByUserJWT(testJWT);

        Assertions.assertThat(actualInferences).isEqualTo(expectedInferences);
    }

    @Test
    public void createInferenceForUserWhenJWTIsNotValidShouldThrowInvalidJWTException() {
        User user = new User();
        user.setEmail("test@test.com");

        Mockito.when(jwtUtil.extractUsername("testJWT"))
                .thenReturn(user.getEmail());

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(InvalidJWTException.class, () -> {
            inferenceService.createInferenceForUser("wrongTestJWT", new MockMultipartFile("test", new byte[0]));
        });

        String expectedMessage = "Invalid JWT.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void createInferenceForUserWhenUserDoesNotExistShouldThrowUserDoesNotExistException() {
        User user = new User();
        user.setId(1l);
        user.setEmail("test@test.com");

        String testJWT = "testJWT";

        Mockito.when(jwtUtil.extractUsername(testJWT))
                .thenReturn(user.getEmail());
        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(UserDoesNotExistException.class, () -> {
            inferenceService.createInferenceForUser("testJWT", new MockMultipartFile("test", new byte[0]));
        });

        String expectedMessage = "User with email test@test.com does not exist.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
