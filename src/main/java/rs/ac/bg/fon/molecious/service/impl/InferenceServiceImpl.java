package rs.ac.bg.fon.molecious.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import rs.ac.bg.fon.molecious.builder.impl.InferenceBuilderImpl;
import rs.ac.bg.fon.molecious.config.security.util.JwtUtil;
import rs.ac.bg.fon.molecious.controller.wrapper.Response;
import rs.ac.bg.fon.molecious.exception.InvalidJWTException;
import rs.ac.bg.fon.molecious.exception.UserDoesNotExistException;
import rs.ac.bg.fon.molecious.model.Inference;
import rs.ac.bg.fon.molecious.model.User;
import rs.ac.bg.fon.molecious.repository.InferenceRepository;
import rs.ac.bg.fon.molecious.repository.UserRepository;
import rs.ac.bg.fon.molecious.service.InferenceService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class InferenceServiceImpl implements InferenceService {

    @Autowired
    private InferenceRepository inferenceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Value("${firebase.project.id}")
    private String firebaseProjectId;
    @Value("${skin.cancer.detection.inference.api.base.url}")
    private String skinCancerDetectionInferenceApiBaseUrl;

    @Override
    public List<Inference> findAllByUserJWT(String JWT) {
        User user = extractUser(JWT);
        return inferenceRepository.findAllByUserId(user.getId());
    }

    @Override
    public Inference createInferenceForUser(String JWT, MultipartFile file) {
        User user = extractUser(JWT);
        LinkedHashMap<String, String> predictions = extractPredictions(file);

        LinkedHashMap<String, String> uploadResponse = uploadImageToFirebase(file);
        String firebaseImageDownloadUrl = extractFirebaseImageDownloadUrl(uploadResponse);

        Inference inference = InferenceBuilderImpl.load()
                .setInferenceUser(user)
                .setProbabilities(predictions)
                .setImageDownloadUrl(firebaseImageDownloadUrl)
                .build();

        return inferenceRepository.save(inference);
    }

    private User extractUser(String JWT) {
        String email = jwtUtil.extractUsername(JWT);
        if (email == null) {
            throw new InvalidJWTException("Invalid JWT.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserDoesNotExistException(
                    new StringBuilder()
                            .append("User with email ")
                            .append(email)
                            .append(" does not exist.")
                            .toString()
            );
        }

        return optionalUser.get();
    }

    private LinkedHashMap<String, String> extractPredictions(MultipartFile file) {
        return (LinkedHashMap<String, String>) performInference(file).getData();
    }

    private Response performInference(MultipartFile file) {
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("file", new FileSystemResource(convert(file)));

        return webClientBuilder.build()
                .post()
                .uri(skinCancerDetectionInferenceApiBaseUrl + "/api/v1/inference")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(data))
                .retrieve()
                .bodyToMono(Response.class)
                .block();
    }

    private File convert(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());

        try {
            convertedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertedFile;
    }

    private LinkedHashMap<String, String> uploadImageToFirebase(MultipartFile file) {
        String firebaseUploadUrl = new StringBuilder()
                .append("https://firebasestorage.clients6.google.com/v0/b/")
                .append(firebaseProjectId)
                .append(".appspot.com/o?uploadType=multipart&name=")
                .append((new Date()).getTime())
                .append(".jpg")
                .toString();

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("file", new FileSystemResource(convert(file)));

        return webClientBuilder.build()
                .post()
                .uri(firebaseUploadUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(data))
                .retrieve()
                .bodyToMono(LinkedHashMap.class)
                .block();
    }

    private String extractFirebaseImageDownloadUrl(LinkedHashMap<String, String> uploadResponse) {
        String name = null;
        String token = null;

        for (String key : uploadResponse.keySet()) {
            if (name != null && token != null) {
                break;
            }

            if (name == null && "name".equals(key)) {
                name = uploadResponse.get(key);
            }

            if (token == null && "downloadTokens".equals(key)) {
                token = uploadResponse.get(key);
            }
        }

        return buildFirebaseImageDownloadUrl(name, token);
    }

    private String buildFirebaseImageDownloadUrl(String name, String token) {
        return new StringBuilder()
                .append("https://firebasestorage.googleapis.com/v0/b/")
                .append(firebaseProjectId)
                .append(".appspot.com/o/")
                .append(name)
                .append("?alt=media&token=")
                .append(token)
                .toString();
    }
}
