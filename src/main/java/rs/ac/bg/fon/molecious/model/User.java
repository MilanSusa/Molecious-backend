package rs.ac.bg.fon.molecious.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "molecious_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    @OneToMany
    private List<Inference> inferences;
}
