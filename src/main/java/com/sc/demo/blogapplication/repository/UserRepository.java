package com.sc.demo.blogapplication.repository;

import com.sc.demo.blogapplication.model.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<BlogUser, Long> {

  BlogUser findByUsername(String username);

}
