package rs.ac.bg.fon.molecious.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.molecious.model.Inference;

import java.util.List;

@Repository
public interface InferenceRepository extends JpaRepository<Inference, Long> {

    List<Inference> findAllByUserId(Long userId);
}
