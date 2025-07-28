package com.forumhub.service;


import com.forumhub.dto.request.UserRequest;
import com.forumhub.dto.response.UserResponse;
import com.forumhub.entity.User;
import com.forumhub.exceptions.ResourceNotFoundException;
import com.forumhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findByActiveTrue()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserResponse(user);
    }

    public UserResponse create(UserRequest request) {
        // Verificar se email já existe
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Verificar se email já existe para outro usuário
        if (!user.getEmail().equals(request.email()) &&
                userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }
        user.setName(request.name());
        user.setEmail(request.email());

        // Só atualiza senha se fornecida
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User updatedUser = userRepository.save(user);
        return new UserResponse(updatedUser);
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Soft delete
        user.setActive(false);
        userRepository.save(user);
    }

    public List<UserResponse> findByRole(User.Role role) {
        return userRepository.findByRoleAndActiveTrue(role)
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public List<UserResponse> searchByName(String name) {
        return userRepository.findByNameContainingIgnoreCaseAndActiveTrue(name)
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

}
