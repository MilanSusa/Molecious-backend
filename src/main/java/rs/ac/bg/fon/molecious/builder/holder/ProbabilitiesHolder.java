package rs.ac.bg.fon.molecious.builder.holder;

import java.util.LinkedHashMap;

public interface ProbabilitiesHolder {

    ImageUrlHolder setProbabilities(LinkedHashMap<String, String> predictions);
}
