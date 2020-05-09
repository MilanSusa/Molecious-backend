package rs.ac.bg.fon.molecious.builder.holder;

import rs.ac.bg.fon.molecious.builder.InferenceBuilder;

import java.util.LinkedHashMap;

public interface ProbabilitiesHolder {

    InferenceBuilder setProbabilities(LinkedHashMap<String, String> predictions);
}
