package rs.ac.bg.fon.molecious.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;
import rs.ac.bg.fon.molecious.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.molecious.model.User;
import rs.ac.bg.fon.molecious.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void signUpWhenUserDoesNotExistShouldReturnCreatedUser() throws Exception {
        User user = new User();
        user.setEmail("test@test.com");

        Mockito.when(userService.signUp(user))
                .thenReturn(user);

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/sign-up")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$..email").value(user.getEmail()));
    }

    @Test
    public void signUpWhenUserExistsShouldThrowUserAlreadyExistsException() {
        User user = new User();
        user.setEmail("test@test.com");

        Mockito.when(userService.signUp(user))
                .thenThrow(UserAlreadyExistsException.class);

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NestedServletException.class, () -> {
            ObjectMapper objectMapper = new ObjectMapper();
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/sign-up")
                    .content(objectMapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON));
        });

        Assertions.assertTrue(exception.getCause() instanceof UserAlreadyExistsException);
    }
}
