package com.sc.demo.blogapplication.specification;

import com.sc.demo.blogapplication.model.Post;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

  private PostSpecification() {
  }

  public static Specification<Post> hasTag(String tag) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(tag, root.get("tags"));
  }
}
