package rs.ac.bg.fon.molecious.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.molecious.service.InferenceService;

@RestController
@RequestMapping("api/v1/inferences")
public class InferenceController {

    @Autowired
    private InferenceService inferenceService;
}
