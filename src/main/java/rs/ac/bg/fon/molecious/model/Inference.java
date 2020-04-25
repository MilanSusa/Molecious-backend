package rs.ac.bg.fon.molecious.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
public class Inference {

    @Id
    @GeneratedValue
    private Long id;
    private String imageUrl;
    private double actinicKeratosesProbability;
    private double basalCellCarcinomaProbability;
    private double benignKeratosisLikeLesionsProbability;
    private double dermatofibromaProbability;
    private double melanomaProbability;
    private double melanocyticNeviProbability;
    private double vascularLesionsProbability;
}
