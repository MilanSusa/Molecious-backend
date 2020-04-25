package rs.ac.bg.fon.molecious.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
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
    @ManyToOne
    @JsonIgnore
    private User user;
}
