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

    @GetMapping("users/{userId}")
    public Response<List<Inference>> findAllByUserId(@PathVariable Long userId) {
        return new Response<>(inferenceService.findAllByUserId(userId));
    }

    @PostMapping
    public Response<Inference> createInferenceForUser(@CookieValue String JWT,
                                                      @RequestParam("file") MultipartFile file) {
        return new Response<>(inferenceService.createInferenceForUser(JWT, file));
    }
}
