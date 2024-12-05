package com.sc.demo.blogapplication.security;

import com.sc.demo.blogapplication.model.BlogUser;
import com.sc.demo.blogapplication.repository.UserRepository;
import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    BlogUser user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            "User not found with username: " + username));
    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(), new ArrayList<>());
  }
}
