package rs.ac.bg.fon.molecious;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.ac.bg.fon.molecious.controller.InferenceController;
import rs.ac.bg.fon.molecious.controller.UserController;

@SpringBootTest
public class MoleciousApplicationTests {

    @Autowired
    private InferenceController inferenceController;
    @Autowired
    private UserController userController;

    @Test
    public void contextLoads() {
    }

    @Test
    public void contextWhenLoadedShouldCreateInferenceController() {
        Assertions.assertThat(inferenceController).isNotNull();
    }

    @Test
    public void contextWhenLoadedShouldCreateUserController() {
        Assertions.assertThat(userController).isNotNull();
    }

}
