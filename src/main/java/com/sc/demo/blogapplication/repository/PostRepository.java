
package com.sc.demo.blogapplication.repository;

import com.sc.demo.blogapplication.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("SELECT p FROM Post p WHERE :tag MEMBER OF p.tags")
  List<Post> findAllByTag(String tag);
}
