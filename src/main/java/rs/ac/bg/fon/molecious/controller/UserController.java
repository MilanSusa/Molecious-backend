package rs.ac.bg.fon.molecious.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.molecious.controller.wrapper.Response;
import rs.ac.bg.fon.molecious.dto.UserDto;
import rs.ac.bg.fon.molecious.model.AuthenticationRequest;
import rs.ac.bg.fon.molecious.model.AuthenticationResponse;
import rs.ac.bg.fon.molecious.model.User;
import rs.ac.bg.fon.molecious.service.UserService;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("authenticate")
    public Response<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return new Response<>(userService.createAuthenticationToken(authenticationRequest));
    }

    @PostMapping("sign-up")
    public Response<User> signUp(@RequestBody User user) {
        return new Response<>(userService.signUp(user));
    }

    @PostMapping("jwt")
    public Response<UserDto> extractUserFromJWT(@CookieValue String JWT) {
        return new Response<>(new UserDto(userService.extractUserFromJWT(JWT)));
    }
}
