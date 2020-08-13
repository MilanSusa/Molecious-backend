package rs.ac.bg.fon.molecious.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.molecious.config.security.util.JwtUtil;
import rs.ac.bg.fon.molecious.exception.InvalidJWTException;
import rs.ac.bg.fon.molecious.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.molecious.exception.UserDoesNotExistException;
import rs.ac.bg.fon.molecious.model.AuthenticationRequest;
import rs.ac.bg.fon.molecious.model.AuthenticationResponse;
import rs.ac.bg.fon.molecious.model.User;
import rs.ac.bg.fon.molecious.repository.UserRepository;
import rs.ac.bg.fon.molecious.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User signUp(User user) {
        String email = user.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    new StringBuilder()
                            .append("User with email ")
                            .append(optionalUser.get().getEmail())
                            .append(" already exists.")
                            .toString()
            );
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }

    @Override
    public User extractUserFromJWT(String JWT) {
        String email = jwtUtil.extractUsername(JWT);
        if (email == null) {
            throw new InvalidJWTException("Invalid JWT.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserDoesNotExistException(
                    new StringBuilder()
                            .append("User with email ")
                            .append(email)
                            .append(" doesn't exist.")
                            .toString()
            );
        }

        return optionalUser.get();
    }
}
