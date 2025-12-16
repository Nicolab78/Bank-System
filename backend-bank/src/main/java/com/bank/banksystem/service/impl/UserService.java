package com.bank.banksystem.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bank.banksystem.dto.user.CreateUserDTO;
import org.springframework.stereotype.Service;

import com.bank.banksystem.dto.user.UpdateUserDTO;
import com.bank.banksystem.dto.user.UserDTO;
import com.bank.banksystem.model.User;
import com.bank.banksystem.model.Role;
import com.bank.banksystem.repository.UserRepository;
import com.bank.banksystem.service.interfaces.IUserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findByEnabled(true);
        if (users.isEmpty()) {
            throw new RuntimeException("No active users found.");
        }
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO createUser(CreateUserDTO createUserDTO) {

        if (userRepository.findByUsername(createUserDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(createUserDTO.getUsername())
                .email(createUserDTO.getEmail())
                .password(createUserDTO.getPassword())
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);
        return mapToDto(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        if (!user.isEnabled()) {
            throw new RuntimeException("User with ID " + id + " is inactive.");
        }

        return mapToDto(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with username " + username + " not found."));

        if (!user.isEnabled()) {
            throw new RuntimeException("User with username " + username + " is inactive.");
        }

        return mapToDto(user);
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        if (!user.isEnabled()) {
            throw new RuntimeException("Cannot update inactive user.");
        }

        user.setUsername(updateUserDTO.getUsername());
        user.setEmail(updateUserDTO.getEmail());

        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            user.setPassword(updateUserDTO.getPassword());
        }

        userRepository.save(user);
        return mapToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        user.setEnabled(false);
        userRepository.save(user);
    }

    private UserDTO mapToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}