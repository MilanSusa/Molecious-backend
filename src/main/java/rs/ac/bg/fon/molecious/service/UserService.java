package rs.ac.bg.fon.molecious.service;

import rs.ac.bg.fon.molecious.model.AuthenticationRequest;
import rs.ac.bg.fon.molecious.model.AuthenticationResponse;
import rs.ac.bg.fon.molecious.model.User;

public interface UserService {

    User signUp(User user);

    AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest);

    User extractUserFromJWT(String JWT);
}
