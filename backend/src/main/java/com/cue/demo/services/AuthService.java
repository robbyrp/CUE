package com.cue.demo.services;

import com.cue.demo.dtos.auth.LoginRequestDTO;
import com.cue.demo.dtos.auth.LoginResponseDTO;
import com.cue.demo.dtos.auth.RegisterUserRequestDTO;
import com.cue.demo.entities.User;
import com.cue.demo.enums.UserRole;
import com.cue.demo.exceptions.UsernameAlreadyInUseException;
import com.cue.demo.repositories.UserRepository;
import com.cue.demo.security.JwtService;
import com.cue.demo.security.UserSecurityAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing authentication and registration processes.
 * Handles credential verification and JWT token generation.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user based on their username and password.
     *
     * @param request DTO containing the username and password.
     * @return A {@link LoginResponseDTO} containing the generated JWT token.
     */
    public LoginResponseDTO login(final LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        String token = jwtService.generateToken(userDetails);
        return new LoginResponseDTO(token);
    }

    /**
     * Registers a new user in the system.
     *
     * @param request DTO containing the data required for registration.
     * @return A {@link LoginResponseDTO} containing the JWT token for the new user.
     * @throws UsernameAlreadyInUseException If the chosen username is already in use.
     */
    @Transactional
    public LoginResponseDTO register(final RegisterUserRequestDTO request)
    throws UsernameAlreadyInUseException {

        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyInUseException(request.username());
        }

        User newUser = User.builder()
                .role(UserRole.USER)
                .username(request.username())
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .city(request.city())
                .build();

        userRepository.save(newUser);

        UserSecurityAdapter adapter = new UserSecurityAdapter(newUser);
        String token = jwtService.generateToken(adapter);
        return new LoginResponseDTO(token);
    }



}
