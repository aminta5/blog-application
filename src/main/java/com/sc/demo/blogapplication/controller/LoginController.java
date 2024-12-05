package com.sc.demo.blogapplication.controller;

import com.sc.demo.blogapplication.dto.LoginRequest;
import com.sc.demo.blogapplication.service.TokenBlacklistService;
import com.sc.demo.blogapplication.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  private final AuthenticationManager authentationManager;
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;
  private final TokenBlacklistService tokenBlacklistService;

  public LoginController(AuthenticationManager authentationManager, JwtUtil jwtUtil,
      UserDetailsService userDetailsService, TokenBlacklistService tokenBlacklistService) {
    this.authentationManager = authentationManager;
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
    this.tokenBlacklistService = tokenBlacklistService;
  }

  @PostMapping("/login")
  public String createAuthenticationToken(@RequestBody LoginRequest authRequest) throws Exception {

    logger.info("Attempting to authenticate user: {}", authRequest.getUsername());
    try {
      authentationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
              authRequest.getPassword()));
    } catch (AuthenticationException e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(
        authRequest.getUsername());
    String token = jwtUtil.generateToken(userDetails.getUsername());
    logger.info("JWT token generated for user: {}", authRequest.getUsername());
    return token;
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {

    String token = extractTokenFromHeader(request);
    if (token != null) {
      // Blacklist the token, using its expiration time
      Date expirationTime = jwtUtil.getExpirationDateFromToken(token);
      tokenBlacklistService.blacklistToken(token, expirationTime);
    }
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      SecurityContextHolder.clearContext();
      logger.info("User logged out successfully");
    }
    return ResponseEntity.ok("You have been logged out successfully.");
  }

  private String extractTokenFromHeader(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      return header.substring(7);
    }
    return null;
  }
}
