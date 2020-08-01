package rs.ac.bg.fon.molecious.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rs.ac.bg.fon.molecious.model.Inference;
import rs.ac.bg.fon.molecious.model.User;

import java.util.List;

@DataJpaTest
public class InferenceRepositoryTests {

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private InferenceRepository inferenceRepository;

    @Test
    public void findAllByUserIdWhenUserDoesNotExistShouldNotReturnInferences() {
        List<Inference> inferences = inferenceRepository.findAllByUserId(1l);
        Assertions.assertThat(inferences.size()).isEqualTo(0);
    }

    @Test
    public void findAllByUserIdWhenUserExistsShouldReturnSingleInference() {
        User savedUser = testEntityManager.persist(new User());

        Inference inference = new Inference();
        inference.setUser(savedUser);
        testEntityManager.persist(inference);

        List<Inference> inferences = inferenceRepository.findAllByUserId(savedUser.getId());
        Assertions.assertThat(inferences.size()).isEqualTo(1);
    }

    @Test
    public void findAllByUserIdWhenWrongIdIsPassedShouldNotReturnInferences() {
        User savedUser = testEntityManager.persist(new User());

        Inference inference = new Inference();
        inference.setUser(savedUser);
        testEntityManager.persist(inference);

        List<Inference> inferences = inferenceRepository.findAllByUserId(savedUser.getId() + 1);
        Assertions.assertThat(inferences.size()).isEqualTo(0);
    }

    @Test
    public void findAllByUserIdWhenUserDoesNotHaveInferencesShouldNotReturnInferences() {
        User savedUser = testEntityManager.persist(new User());

        Inference inference = new Inference();
        testEntityManager.persist(inference);

        List<Inference> inferences = inferenceRepository.findAllByUserId(savedUser.getId());
        Assertions.assertThat(inferences.size()).isEqualTo(0);
    }

    @Test
    public void findAllByUserIdWhenUserExistsShouldReturnMultipleInferences() {
        User savedUser = testEntityManager.persist(new User());

        Inference firstInference = new Inference();
        firstInference.setUser(savedUser);
        testEntityManager.persist(firstInference);
        Inference secondInference = new Inference();
        secondInference.setUser(savedUser);
        testEntityManager.persist(secondInference);

        List<Inference> inferences = inferenceRepository.findAllByUserId(savedUser.getId());
        Assertions.assertThat(inferences.size()).isEqualTo(2);
    }
}
