package com.sc.demo.blogapplication.controller;

import com.sc.demo.blogapplication.dto.UserDTO;
import com.sc.demo.blogapplication.model.BlogUser;
import com.sc.demo.blogapplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);


  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Register a new user")
  @PostMapping("/register")
  public ResponseEntity<BlogUser> createUser(@Valid @RequestBody UserDTO userDTO) {

    BlogUser createdUser = userService.createUser(userDTO);
    logger.info("User registered successfully with username: {}", createdUser.getUsername());
    return ResponseEntity.ok(createdUser);
  }

  @Operation(summary = "Retrieve user by username")
  @GetMapping("/{username}")
  public ResponseEntity<BlogUser> getUser(@PathVariable String username) {

    BlogUser user = userService.getUserByUsername(username);
    logger.info("User retrieved successfully with username: {}", username);
    return ResponseEntity.ok(user);
  }

  @Operation(summary = "Update user details")
  @PutMapping("/{id}")
  public ResponseEntity<BlogUser> updateUser(@PathVariable UUID id,
      @Valid @RequestBody UserDTO userDTO) {
    BlogUser updatedUser = userService.updateUser(id, userDTO);
    logger.info("User updated successfully with ID: {}", id);
    return ResponseEntity.ok(updatedUser);
  }

  @Operation(summary = "Delete user")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
    logger.info("User deleted successfully with ID: {}", id);
    return ResponseEntity.ok("User deleted successfully");
  }
}
