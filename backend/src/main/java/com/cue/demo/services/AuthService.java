package com.cue.demo.services;

import com.cue.demo.dtos.*;
import com.cue.demo.entities.User;
import com.cue.demo.enums.UserRole;
import com.cue.demo.exceptions.UserNotFoundException;
import com.cue.demo.exceptions.UsernameAlreadyInUseException;
import com.cue.demo.mapper.ReviewMapper;
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

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewMapper reviewMapper;

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

    public UserProfileDTO getCurrentUserProfileById(final Long userId)
    throws UserNotFoundException {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Set<ReviewDTO> reviewDTOS = user.getReviews().stream()
                .map(reviewMapper::fromReviewToReviewDTO)
                .collect(Collectors.toSet());

        return UserProfileDTO.builder()
                .id(user.getId())
                .role(user.getRole())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .city(user.getCity())
                .profilePictureUrl(user.getProfilePictureUrl())
                .bio(user.getBio())
                .reviews(reviewDTOS)
                .build();
    }

}
