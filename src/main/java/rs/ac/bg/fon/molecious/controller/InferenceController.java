package rs.ac.bg.fon.molecious.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.molecious.controller.wrapper.Response;
import rs.ac.bg.fon.molecious.model.Inference;
import rs.ac.bg.fon.molecious.service.InferenceService;

import java.util.List;

@RestController
@RequestMapping("api/v1/inferences")
public class InferenceController {

    @Autowired
    private InferenceService inferenceService;

    @GetMapping("users/jwt")
    public Response<List<Inference>> findAllByUserJWT(@CookieValue String JWT) {
        return new Response<>(inferenceService.findAllByUserJWT(JWT));
    }

    @PostMapping
    public Response<Inference> createInferenceForUser(@CookieValue String JWT,
                                                      @RequestParam("file") MultipartFile file) {
        return new Response<>(inferenceService.createInferenceForUser(JWT, file));
    }
}
