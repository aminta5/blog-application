package com.sc.demo.blogapplication.service;

import com.sc.demo.blogapplication.dto.PostDTO;
import com.sc.demo.blogapplication.model.BlogUser;
import com.sc.demo.blogapplication.model.Post;
import com.sc.demo.blogapplication.repository.PostRepository;
import com.sc.demo.blogapplication.repository.UserRepository;
import com.sc.demo.blogapplication.specification.PostSpecification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostService(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public PostDTO createPost(PostDTO postDto) {

    BlogUser user = userRepository.findById(postDto.userId()).orElseThrow(() -> new RuntimeException(
        "User not found"));
    Post post = Post.builder()
        .title(postDto.title())
        .content(postDto.content())
        .user(user)
        .build();

    postRepository.save(post);

    return postDto;
  }

  public List<PostDTO> getAllBlogPosts() {
    return postRepository.findAll().stream().map(post -> {
      String summary = post.getContent().length() > 50 ? post.getContent().substring(0, 50) + "..."
          : post.getContent();
      return new PostDTO(post.getTitle(), summary, post.getUser().getId());
    }).toList();
  }

 // public List<PostDTO>


  public Optional<Post> updatePost(UUID id, PostDTO updatedPost) {
    return postRepository.findById(id)
        .map(post -> {
          post.setTitle(updatedPost.title());
          post.setContent(updatedPost.content());
          return postRepository.save(post);
        });
  }

  @Transactional
  public Optional<Post> addTagToPost(UUID id, String tag) {
    return postRepository.findById(id).map(post -> {
      post.getTags().add(tag);
      return postRepository.save(post);
    });
  }

  @Transactional
  public Optional<Post> removeTagFromPost(UUID id, String tag) {
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
