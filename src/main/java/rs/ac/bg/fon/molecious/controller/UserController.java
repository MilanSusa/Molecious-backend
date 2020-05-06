package rs.ac.bg.fon.molecious.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.molecious.config.security.util.JwtUtil;
import rs.ac.bg.fon.molecious.controller.wrapper.Response;
import rs.ac.bg.fon.molecious.dto.UserDto;
import rs.ac.bg.fon.molecious.model.AuthenticationRequest;
import rs.ac.bg.fon.molecious.model.AuthenticationResponse;
import rs.ac.bg.fon.molecious.model.User;
import rs.ac.bg.fon.molecious.service.UserService;
import rs.ac.bg.fon.molecious.service.impl.UserDetailsServiceImpl;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("authenticate")
    public Response<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        return new Response<>(new AuthenticationResponse(jwt));
    }

    @PostMapping("sign-up")
    public Response<User> signUp(@RequestBody User user) {
        return new Response<>(userService.signUp(user));
    }

    @PostMapping("jwt")
    public Response<UserDto> extractUserFromJwt(@CookieValue String JWT) {
        String email = jwtUtil.extractUsername(JWT);
        return new Response<>(new UserDto(userService.findByEmail(email)));
    }
}
