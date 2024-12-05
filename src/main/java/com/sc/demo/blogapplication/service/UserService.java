package com.sc.demo.blogapplication.service;

import com.sc.demo.blogapplication.dto.UserDTO;
import com.sc.demo.blogapplication.model.BlogUser;
import com.sc.demo.blogapplication.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public BlogUser createUser(UserDTO userDTO) {
    BlogUser blogUser = new BlogUser();
    blogUser.setUsername(userDTO.username());
    blogUser.setPassword(passwordEncoder.encode(userDTO.password()));
    blogUser.setDisplayName(userDTO.displayName());
    return userRepository.save(blogUser);
  }

  public BlogUser getUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  public Optional<BlogUser> getUserById(UUID id) {
    return userRepository.findById(id);
  }

  public BlogUser updateUser(UUID user, UserDTO userDTO) {
    return userRepository.findById(user).map(blogUser -> {
      blogUser.setUsername(userDTO.username());
      blogUser.setPassword(userDTO.password());
      blogUser.setDisplayName(userDTO.displayName());
      return userRepository.save(blogUser);
    }).orElseThrow(() -> new RuntimeException("User not found"));

  }

  public void deleteUser(UUID userId) {
    BlogUser blogUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User does not found"));
    if (blogUser != null) {
      userRepository.delete(blogUser);
    }
  }


}
