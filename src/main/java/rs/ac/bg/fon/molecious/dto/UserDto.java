package rs.ac.bg.fon.molecious.dto;

import lombok.Data;
import rs.ac.bg.fon.molecious.model.User;

@Data
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;

    public UserDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
