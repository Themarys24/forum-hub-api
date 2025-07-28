package com.forumhub.service;

import com.forumhub.dto.request.LoginRequest;
import com.forumhub.dto.request.UserRequest;
import com.forumhub.dto.response.TokenResponse;
import com.forumhub.dto.response.UserResponse;
import com.forumhub.entity.User;
import com.forumhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginRequest request){
        try {
            //Authentication
            var authToken = new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
            );

            Authentication authentication = authenticationManager.authenticate(authToken);

            //Generate Token - Search user by email
            User user = userRepository.findByEmailAndActiveTrue(request.email())
                    .orElseThrow(() -> new RuntimeException("User not found or inactive"));

            String token = tokenService.generateToken(user);

            //Create Response
            UserResponse userResponse = new UserResponse(user);

            return new TokenResponse(token, userResponse);

        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    public UserResponse register(UserRequest request) {
        //Check if email already exists
        if (userRepository.existsByEmail(request.email())){
            throw new RuntimeException("Email already exists");
        }

        //Create User
        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }
}
