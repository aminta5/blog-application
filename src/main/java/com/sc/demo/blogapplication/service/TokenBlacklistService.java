package com.sc.demo.blogapplication.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

  private final RedisTemplate<String, String> redisTemplate;

  public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void blacklistToken(String token, Date expirationTime) {
    ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
    LocalDateTime expirationLocalDateTime = expirationTime.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
    Duration duration = Duration.between(LocalDateTime.now(ZoneOffset.UTC), expirationLocalDateTime);
    valueOps.set(token, "BLACKLISTED", duration);
  }

  public boolean isTokenBlacklisted(String token) {
    ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
    return valueOps.get(token) != null;
  }

}
