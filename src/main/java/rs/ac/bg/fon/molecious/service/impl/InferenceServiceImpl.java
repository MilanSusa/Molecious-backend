package rs.ac.bg.fon.molecious.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.molecious.repository.InferenceRepository;
import rs.ac.bg.fon.molecious.service.InferenceService;

@Service
public class InferenceServiceImpl implements InferenceService {

    @Autowired
    private InferenceRepository inferenceRepository;
}
