package com.sc.demo.blogapplication.repository;

import com.sc.demo.blogapplication.model.BlogUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<BlogUser, UUID> {

  Optional<BlogUser> findByUsername(String username);

}
