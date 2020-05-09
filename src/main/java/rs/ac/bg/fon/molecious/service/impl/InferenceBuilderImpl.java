package rs.ac.bg.fon.molecious.service.impl;

import lombok.Setter;
import rs.ac.bg.fon.molecious.builder.InferenceBuilder;
import rs.ac.bg.fon.molecious.builder.holder.ProbabilitiesHolder;
import rs.ac.bg.fon.molecious.builder.holder.UserHolder;
import rs.ac.bg.fon.molecious.model.Inference;
import rs.ac.bg.fon.molecious.model.User;

import java.util.LinkedHashMap;

@Setter
public class InferenceBuilderImpl implements UserHolder, ProbabilitiesHolder, InferenceBuilder {

    private String imageUrl;
    private double actinicKeratosesProbability;
    private double basalCellCarcinomaProbability;
    private double benignKeratosisLikeLesionsProbability;
    private double dermatofibromaProbability;
    private double melanomaProbability;
    private double melanocyticNeviProbability;
    private double vascularLesionsProbability;
    private User user;

    private InferenceBuilderImpl() {
    }

    public static UserHolder load() {
        return new InferenceBuilderImpl();
    }

    @Override
    public ProbabilitiesHolder setInferenceUser(User user) {
        setUser(user);
        return this;
    }

    @Override
    public InferenceBuilder setProbabilities(LinkedHashMap<String, String> predictions) {
        predictions.forEach((clazz, probability) -> {
            switch (clazz) {
                case "akiec":
                    setActinicKeratosesProbability(Double.valueOf(probability));
                    break;
                case "bcc":
                    setBasalCellCarcinomaProbability(Double.valueOf(probability));
                    break;
                case "bkl":
                    setBenignKeratosisLikeLesionsProbability(Double.valueOf(probability));
                    break;
                case "df":
                    setDermatofibromaProbability(Double.valueOf(probability));
                    break;
                case "mel":
                    setMelanomaProbability(Double.valueOf(probability));
                    break;
                case "nv":
                    setMelanocyticNeviProbability(Double.valueOf(probability));
                    break;
                case "vasc":
                    setVascularLesionsProbability(Double.valueOf(probability));
                    break;
                default:
                    break;
            }
        });

        return this;
    }

    @Override
    public Inference build() {
        Inference inference = new Inference();
        inference.setImageUrl(imageUrl);
        inference.setActinicKeratosesProbability(actinicKeratosesProbability);
        inference.setBasalCellCarcinomaProbability(basalCellCarcinomaProbability);
        inference.setBenignKeratosisLikeLesionsProbability(benignKeratosisLikeLesionsProbability);
        inference.setDermatofibromaProbability(dermatofibromaProbability);
        inference.setMelanomaProbability(melanomaProbability);
        inference.setMelanocyticNeviProbability(melanocyticNeviProbability);
        inference.setVascularLesionsProbability(vascularLesionsProbability);
        inference.setUser(user);
        return inference;
    }
}
