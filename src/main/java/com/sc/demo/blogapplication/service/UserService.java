package com.sc.demo.blogapplication.service;

import com.sc.demo.blogapplication.dto.UserDTO;
import com.sc.demo.blogapplication.model.BlogUser;
import com.sc.demo.blogapplication.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public BlogUser createUser(UserDTO userDto) {
    BlogUser blogUser = new BlogUser();
    blogUser.setUsername(userDto.username());
    blogUser.setPassword(userDto.password());
    blogUser.setDisplayName(userDto.displayName());
    return userRepository.save(blogUser);
  }

  public BlogUser getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public BlogUser updateUser(String username, UserDTO userDto) {
    BlogUser blogUser = userRepository.findByUsername(username);
    if (blogUser != null) {
      blogUser.setPassword(userDto.password());
      blogUser.setDisplayName(userDto.displayName());
      return userRepository.save(blogUser);
    }
    return null;
  }

  public void deleteUser(String username) {
    BlogUser blogUser = userRepository.findByUsername(username);
    if (blogUser != null) {
      userRepository.delete(blogUser);
    }
  }


}
