package com.sc.demo.blogapplication.controller;

import com.sc.demo.blogapplication.dto.UserDTO;
import com.sc.demo.blogapplication.model.BlogUser;
import com.sc.demo.blogapplication.service.UserService;
import jakarta.validation.Valid;
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

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<BlogUser> createUser(@Valid @RequestBody UserDTO userDto) {

    return ResponseEntity.ok(userService.createUser(userDto));
  }

  @GetMapping("/{username}")
  public ResponseEntity<BlogUser> getUser(@PathVariable String username) {
    return ResponseEntity.ok(userService.getUserByUsername(username));
  }

  @PutMapping("/{username}")
  public ResponseEntity<BlogUser> updateUser(@PathVariable String username,
      @Valid @RequestBody UserDTO userDto) {
    return ResponseEntity.ok(userService.updateUser(username, userDto));
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<String> deleteUser(@PathVariable String username) {
    userService.deleteUser(username);
    return ResponseEntity.ok("User deleted successfully");
  }
}
