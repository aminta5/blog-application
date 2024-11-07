package com.sc.demo.blogapplication.service;

import com.sc.demo.blogapplication.dto.PostDTO;
import com.sc.demo.blogapplication.model.Post;
import com.sc.demo.blogapplication.repository.PostRepository;
import com.sc.demo.blogapplication.specification.PostSpecification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public Post createPost(Post post) {
    return postRepository.save(post);
  }

  public List<PostDTO> getAllBlogPosts() {
    return postRepository.findAll().stream().map(post -> {
      String summary = post.getContent().length() > 50 ? post.getContent().substring(0, 50) + "..."
          : post.getContent();
      return new PostDTO(post.getTitle(), summary);
    }).toList();
  }


  public Optional<Post> updatePost(long id, PostDTO updatedPost) {
    return postRepository.findById(id)
        .map(post -> {
          post.setTitle(updatedPost.title());
          post.setContent(updatedPost.content());
          return postRepository.save(post);
        });
  }

  @Transactional
  public Optional<Post> addTagToPost(long id, String tag) {
    return postRepository.findById(id).map(post -> {
      post.getTags().add(tag);
      return postRepository.save(post);
    });
  }

  @Transactional
  public Optional<Post> removeTagFromPost(long id, String tag) {
    return postRepository.findById(id).map(post -> {
      post.getTags().remove(tag);
      return postRepository.save(post);
    });
  }

  public List<Post> getAllPostsByTag(String tag) {
    Specification<Post> tagSpec = PostSpecification.hasTag(tag);
    return postRepository.findAll(tagSpec);
  }
}
