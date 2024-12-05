package com.sc.demo.blogapplication.controller;

import com.sc.demo.blogapplication.dto.PostDTO;
import com.sc.demo.blogapplication.model.Post;
import com.sc.demo.blogapplication.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @Operation(summary = "Create a new post")
  @PostMapping
  public ResponseEntity<Post> createPost(@Valid @RequestBody PostDTO postDTO) {

    Post createdPost = postService.createPost(postDTO);
    logger.info("Post created successfully with id: {}", createdPost.getId());
    return ResponseEntity.ok(createdPost);
  }

  @Operation(summary = "Retrieve all posts")
  @GetMapping
  public ResponseEntity<List<PostDTO>> getAllPosts() {
    return ResponseEntity.ok(postService.getAllBlogPosts());
  }

  @Operation(summary = "Update a post by ID")
  @PutMapping("/{id}")
  public ResponseEntity<Post> updatePost(@PathVariable UUID id,
      @Valid @RequestBody PostDTO postDTO, String username) {

    logger.info("Updating post with ID: {}", id);
    Optional<Post> updatedPost = postService.updatePost(id, postDTO, username);
    if (updatedPost.isPresent()) {
      logger.info("Post updated successfully with ID: {}", id);
      return ResponseEntity.ok(updatedPost.get());
    } else {
      logger.warn("Post not found with ID: {}", id);
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Add or remove a tag from a post")
  @PatchMapping("/{id}/tags")
  public ResponseEntity<Post> addOrRemoveTag(@PathVariable UUID id, @RequestParam String tag,
      @RequestParam boolean add) {
    Optional<Post> updatedPost =
        add ? postService.addTagToPost(id, tag) : postService.removeTagFromPost(id, tag);
    return updatedPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "Retrieve all posts by tag")
  @GetMapping(params = "tag")
  public ResponseEntity<List<Post>> getPostsByTag(@RequestParam String tag) {
    return ResponseEntity.ok(postService.getAllPostsByTag(tag));
  }

  @Operation(summary = "Delete a post by ID")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deletePost(@PathVariable UUID id) {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    String message = postService.deletePost(id, username) ? "Post deleted successfully"
        : "Post not found";
    logger.info(message);
    return ResponseEntity.ok(message);
  }
}
