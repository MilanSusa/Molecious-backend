package rs.ac.bg.fon.molecious.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import rs.ac.bg.fon.molecious.model.User;
import rs.ac.bg.fon.molecious.repository.UserRepository;
import rs.ac.bg.fon.molecious.service.impl.UserDetailsServiceImpl;

import java.util.Collections;
import java.util.Optional;

@SpringBootTest
public class UserDetailsServiceImplTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void loadUserByUsernameWhenUserExistsShouldReturnUserDetails() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");

        Mockito.when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        UserDetails actualUserDetails = userDetailsService.loadUserByUsername("test@test.com");
        UserDetails expectedUserDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );

        Assertions.assertThat(actualUserDetails).isEqualTo(expectedUserDetails);
    }

    @Test
    public void loadUserByUsernameWhenUserDoesNotExistShouldThrowUsernameNotFoundException() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");

        Mockito.when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("test@test.com");
        });

        String expectedMessage = "User with email test@test.com does not exist.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
