package rs.ac.bg.fon.molecious.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;
import rs.ac.bg.fon.molecious.config.security.util.JwtUtil;
import rs.ac.bg.fon.molecious.exception.InvalidJWTException;
import rs.ac.bg.fon.molecious.exception.UserDoesNotExistException;
import rs.ac.bg.fon.molecious.model.Inference;
import rs.ac.bg.fon.molecious.service.InferenceService;

import javax.servlet.http.Cookie;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class InferenceControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InferenceService inferenceService;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void findAllByUserJWTWhenJWTIsNotValidShouldThrowInvalidJWTException() {
        String jwt = "wrongTestJWT";

        Mockito.when(inferenceService.findAllByUserJWT(jwt))
                .thenThrow(InvalidJWTException.class);

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inferences/users/jwt")
                    .cookie(new Cookie("JWT", jwt)));
        });

        Assertions.assertTrue(exception.getCause() instanceof InvalidJWTException);
    }

    @Test
    public void findAllByUserJWTWhenUserDoesNotExistShouldThrowUserDoesNotExistException() {
        String jwt = "testJWT";

        Mockito.when(inferenceService.findAllByUserJWT(jwt))
                .thenThrow(UserDoesNotExistException.class);

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inferences/users/jwt")
                    .cookie(new Cookie("JWT", jwt)));
        });

        Assertions.assertTrue(exception.getCause() instanceof UserDoesNotExistException);
    }

    @Test
    public void findAllByUserJWTWhenUserExistsShouldReturnSingleInference() throws Exception {
        String jwt = "testJWT";
        Inference inference = new Inference();
        inference.setImageUrl("https://test.com");

        Mockito.when(inferenceService.findAllByUserJWT(jwt))
                .thenReturn(List.of(inference));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inferences/users/jwt")
                .cookie(new Cookie("JWT", jwt)))
                .andExpect(MockMvcResultMatchers.jsonPath("$..imageUrl").value(inference.getImageUrl()));
    }

    @Test
    public void findAllByUserJWTWhenUserExistsShouldReturnMultipleInferences() throws Exception {
        String jwt = "testJWT";
        Inference firstInference = new Inference();
        firstInference.setImageUrl("https://first-test.com");
        Inference secondInference = new Inference();
        secondInference.setImageUrl("https://second-test.com");

        Mockito.when(inferenceService.findAllByUserJWT(jwt))
                .thenReturn(List.of(firstInference, secondInference));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inferences/users/jwt")
                .cookie(new Cookie("JWT", jwt)))
                .andExpect(MockMvcResultMatchers.jsonPath("$..imageUrl").isArray());
    }

    @Test
    public void createInferenceForUserWhenJWTIsNotValidShouldThrowInvalidJWTException() {
        String jwt = "wrongTestJWT";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", new byte[0]);

        Mockito.when(inferenceService.createInferenceForUser(jwt, mockMultipartFile))
                .thenThrow(InvalidJWTException.class);

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/inferences")
                    .file(mockMultipartFile)
                    .cookie(new Cookie("JWT", jwt)));
        });

        Assertions.assertTrue(exception.getCause() instanceof InvalidJWTException);
    }

    @Test
    public void createInferenceForUserWhenUserDoesNotExistShouldThrowUserDoesNotExistException() {
        String jwt = "testJWT";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", new byte[0]);

        Mockito.when(inferenceService.createInferenceForUser(jwt, mockMultipartFile))
                .thenThrow(UserDoesNotExistException.class);

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/inferences")
                    .file(mockMultipartFile)
                    .cookie(new Cookie("JWT", jwt)));
        });

        Assertions.assertTrue(exception.getCause() instanceof UserDoesNotExistException);
    }

    @Test
    public void createInferenceForUserWhenUserExistsShouldCreateInference() throws Exception {
        String jwt = "testJWT";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", new byte[0]);
        Inference inference = new Inference();
        inference.setImageUrl("https://test.com");

        Mockito.when(inferenceService.createInferenceForUser(jwt, mockMultipartFile))
                .thenReturn(inference);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/inferences")
                .file(mockMultipartFile)
                .cookie(new Cookie("JWT", jwt)))
                .andExpect(MockMvcResultMatchers.jsonPath("$..imageUrl").value(inference.getImageUrl()));
    }
}
