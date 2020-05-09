package rs.ac.bg.fon.molecious.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import rs.ac.bg.fon.molecious.config.security.util.JwtUtil;
import rs.ac.bg.fon.molecious.controller.wrapper.Response;
import rs.ac.bg.fon.molecious.model.Inference;
import rs.ac.bg.fon.molecious.model.User;
import rs.ac.bg.fon.molecious.repository.InferenceRepository;
import rs.ac.bg.fon.molecious.service.InferenceService;
import rs.ac.bg.fon.molecious.service.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class InferenceServiceImpl implements InferenceService {

    @Autowired
    private InferenceRepository inferenceRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public List<Inference> findAllByUserId(Long userId) {
        return inferenceRepository.findAllByUserId(userId);
    }

    @Override
    public Inference createInferenceForUser(String JWT, MultipartFile file) {
        User user = extractUser(JWT);
        LinkedHashMap<String, String> predictions = extractPredictions(file);

        Inference inference = InferenceBuilderImpl.load()
                .setInferenceUser(user)
                .setProbabilities(predictions)
                .build();

        return inferenceRepository.save(inference);
    }

    private User extractUser(String JWT) {
        String email = jwtUtil.extractUsername(JWT);
        return userService.findByEmail(email);
    }

    private LinkedHashMap<String, String> extractPredictions(MultipartFile file) {
        return (LinkedHashMap<String, String>) performInference(file).getData();
    }

    private Response performInference(MultipartFile file) {
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("file", new FileSystemResource(convert(file)));

        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8000/api/v1/inference")
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
}
