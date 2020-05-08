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
        Inference inference = new Inference();
        inference = setInferenceUser(inference, JWT);
        inference = setProbabilities(inference, file);
        return inferenceRepository.save(inference);
    }

    private Inference setInferenceUser(Inference inference, String JWT) {
        String email = jwtUtil.extractUsername(JWT);
        User user = userService.findByEmail(email);
        inference.setUser(user);
        return inference;
    }

    private Inference setProbabilities(Inference inference, MultipartFile file) {
        LinkedHashMap<String, String> predictions = (LinkedHashMap<String, String>) this.performInference(file).getData();

        predictions.forEach((clazz, probability) -> {
            switch (clazz) {
                case "akiec":
                    inference.setActinicKeratosesProbability(Double.valueOf(probability));
                    break;
                case "bcc":
                    inference.setBasalCellCarcinomaProbability(Double.valueOf(probability));
                    break;
                case "bkl":
                    inference.setBenignKeratosisLikeLesionsProbability(Double.valueOf(probability));
                    break;
                case "df":
                    inference.setDermatofibromaProbability(Double.valueOf(probability));
                    break;
                case "mel":
                    inference.setMelanomaProbability(Double.valueOf(probability));
                    break;
                case "nv":
                    inference.setMelanocyticNeviProbability(Double.valueOf(probability));
                    break;
                case "vasc":
                    inference.setVascularLesionsProbability(Double.valueOf(probability));
                    break;
                default:
                    break;
            }
        });

        return inference;
    }

    private Response performInference(MultipartFile file) {
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("file", new FileSystemResource(this.convert(file)));

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
