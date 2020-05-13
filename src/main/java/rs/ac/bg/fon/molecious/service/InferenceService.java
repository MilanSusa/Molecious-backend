package rs.ac.bg.fon.molecious.service;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.molecious.model.Inference;

import java.util.List;

public interface InferenceService {

    List<Inference> findAllByUserJWT(String JWT);

    Inference createInferenceForUser(String JWT, MultipartFile file);
}
