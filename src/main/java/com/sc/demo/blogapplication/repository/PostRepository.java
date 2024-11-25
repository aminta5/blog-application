
package com.sc.demo.blogapplication.repository;

import com.sc.demo.blogapplication.model.Post;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {

}
