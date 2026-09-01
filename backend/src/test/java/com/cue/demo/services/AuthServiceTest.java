package com.cue.demo.services;

import com.cue.demo.dtos.auth.LoginRequestDTO;
import com.cue.demo.dtos.auth.LoginResponseDTO;
import com.cue.demo.dtos.auth.RegisterUserRequestDTO;
import com.cue.demo.entities.User;
import com.cue.demo.repositories.UserRepository;
import com.cue.demo.security.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserDetailsService userDetailsService;
    @Mock private JwtService jwtService;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    /**
     * Test case for successful user login.
     * Verifies that the authentication manager is called and a JWT token is returned.
     */
    @Test
    void givenValidCredentials_whenLogin_thenReturnLoginResponse() {
        LoginRequestDTO request = new LoginRequestDTO("user", "pass");
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        String expectedToken = "jwt-token";

        Mockito.when(userDetailsService.loadUserByUsername(request.username())).thenReturn(userDetails);
        Mockito.when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);

        LoginResponseDTO response = authService.login(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedToken, response.token());

        Mockito.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(userDetailsService).loadUserByUsername(request.username());
        Mockito.verify(jwtService).generateToken(userDetails);
    }

    /**
     * Test case for successful user registration.
     * Verifies that the user is saved and a JWT token is returned.
     */
    @Test
    void givenValidRegistrationData_whenRegister_thenReturnLoginResponse() {
        RegisterUserRequestDTO request = new RegisterUserRequestDTO(
                "newUser", "password", "First", "Last", "email@test.com", "City"
        );
        String encodedPassword = "encodedPassword";
        String expectedToken = "jwt-token";

        Mockito.when(userRepository.existsByUsername(request.username())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        Mockito.when(jwtService.generateToken(any())).thenReturn(expectedToken);

        LoginResponseDTO response = authService.register(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedToken, response.token());

        Mockito.verify(userRepository).existsByUsername(request.username());
        Mockito.verify(passwordEncoder).encode(request.password());
        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(jwtService).generateToken(any());
    }
}
