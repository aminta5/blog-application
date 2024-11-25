package com.sc.demo.blogapplication.repository;

import com.sc.demo.blogapplication.model.BlogUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<BlogUser, UUID> {

  BlogUser findByUsername(String username);

}
