package com.sc.demo.blogapplication.controller;

import com.sc.demo.blogapplication.dto.PostDTO;
import com.sc.demo.blogapplication.model.Post;
import com.sc.demo.blogapplication.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
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

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDto) {

    return ResponseEntity.ok(postService.createPost(postDto));
  }

  @GetMapping
  public ResponseEntity<List<PostDTO>> getAllPosts() {
    return ResponseEntity.ok(postService.getAllBlogPosts());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Post> updatePost(@PathVariable UUID id,
      @Valid @RequestBody PostDTO postDTO) {
    Optional<Post> updatedPost = postService.updatePost(id, postDTO);
    return updatedPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PatchMapping("/{id}/tags")
  public ResponseEntity<Post> addOrRemoveTag(@PathVariable UUID id, @RequestParam String tag,
      @RequestParam boolean add) {
    Optional<Post> updatedPost =
        add ? postService.addTagToPost(id, tag) : postService.removeTagFromPost(id, tag);
    return updatedPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping(params = "tag")
  public ResponseEntity<List<Post>> getPostsByTag(@RequestParam String tag) {
    return ResponseEntity.ok(postService.getAllPostsByTag(tag));
  }
}
