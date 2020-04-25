package rs.ac.bg.fon.molecious.service;

import rs.ac.bg.fon.molecious.model.Inference;

import java.util.List;

public interface InferenceService {

    List<Inference> findAllByUserId(Long userId);
}
