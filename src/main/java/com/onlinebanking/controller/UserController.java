package com.onlinebanking.controller;

import com.onlinebanking.dto.UserResponse;
import com.onlinebanking.entity.User;
import com.onlinebanking.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create User
    @PostMapping("/users")
    public UserResponse createUser(
            @Valid @RequestBody User user) {

        User savedUser = userService.saveUser(user);

        return convertToResponse(savedUser);
    }

    // Get All Users
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {

        return userService.getAllUsers()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get User By ID
    @GetMapping("/users/{id}")
    public UserResponse getUserById(
            @PathVariable Long id) {

        User user = userService.getUserById(id);

        return convertToResponse(user);
    }

    // Update User
    @PutMapping("/users/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user) {

        User updatedUser =
                userService.updateUser(id, user);

        return convertToResponse(updatedUser);
    }

    // Delete User
    @DeleteMapping("/users/{id}")
    public String deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return "User deleted successfully";
    }

    // Convert User Entity to UserResponse DTO
    private UserResponse convertToResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}