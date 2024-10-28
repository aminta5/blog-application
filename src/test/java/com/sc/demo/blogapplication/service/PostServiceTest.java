package com.sc.demo.blogapplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sc.demo.blogapplication.dto.PostDTO;
import com.sc.demo.blogapplication.model.Post;
import com.sc.demo.blogapplication.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private PostService postService;

  @Test
  void createPostSuccessfully() {
    Post post = new Post();
    when(postRepository.save(post)).thenReturn(post);

    Post result = postService.createPost(post);

    assertEquals(post, result);
    verify(postRepository, times(1)).save(post);
  }

  @Test
  void getAllBlogPostsSuccessfully() {
    List<Post> posts = List.of(Post.builder().title("title").content("content").build(),
        Post.builder().title("title1").content("content1").build());
    when(postRepository.findAll()).thenReturn(posts);

    List<PostDTO> result = postService.getAllBlogPosts();

    assertEquals(posts.get(0).getContent(), result.get(0).content());
    assertEquals(posts.get(1).getContent(), result.get(1).content());
    verify(postRepository, times(1)).findAll();
  }

  @Test
  void updatePostSuccessfully() {
    long id = 1L;
    PostDTO updatedPost = new PostDTO("New Title", "New Content");
    Post post = new Post();
    when(postRepository.findById(id)).thenReturn(Optional.of(post));
    when(postRepository.save(post)).thenReturn(post);

    Optional<Post> result = postService.updatePost(id, updatedPost);

    assertTrue(result.isPresent());
    assertEquals(updatedPost.title(), result.get().getTitle());
    assertEquals(updatedPost.content(), result.get().getContent());
    verify(postRepository, times(1)).findById(id);
    verify(postRepository, times(1)).save(post);
  }

  @Test
  void updatePostNotFound() {
    long id = 1L;
    PostDTO updatedPost = new PostDTO("New Title", "New Content");
    when(postRepository.findById(id)).thenReturn(Optional.empty());

    Optional<Post> result = postService.updatePost(id, updatedPost);

    assertFalse(result.isPresent());
    verify(postRepository, times(1)).findById(id);
    verify(postRepository, never()).save(any(Post.class));
  }

  @Test
  void addTagToPostSuccessfully() {
    long id = 1L;
    String tag = "tag";
    Post post = new Post();
    when(postRepository.findById(id)).thenReturn(Optional.of(post));

    Optional<Post> result = postService.addTagToPost(id, tag);

    assertTrue(result.isPresent());
    assertTrue(result.get().getTags().contains(tag));
    verify(postRepository, times(1)).findById(id);
  }

  @Test
  void addTagToPostNotFound() {
    long id = 1L;
    String tag = "tag";
    when(postRepository.findById(id)).thenReturn(Optional.empty());

    Optional<Post> result = postService.addTagToPost(id, tag);

    assertFalse(result.isPresent());
    verify(postRepository, times(1)).findById(id);
  }

  @Test
  void removeTagFromPostSuccessfully() {
    long id = 1L;
    String tag = "tag";
    Post post = new Post();
    post.getTags().add(tag);
    when(postRepository.findById(id)).thenReturn(Optional.of(post));

    Optional<Post> result = postService.removeTagFromPost(id, tag);

    assertTrue(result.isPresent());
    assertFalse(result.get().getTags().contains(tag));
    verify(postRepository, times(1)).findById(id);
  }

  @Test
  void removeTagFromPostNotFound() {
    long id = 1L;
    String tag = "tag";
    when(postRepository.findById(id)).thenReturn(Optional.empty());

    Optional<Post> result = postService.removeTagFromPost(id, tag);

    assertFalse(result.isPresent());
    verify(postRepository, times(1)).findById(id);
  }

  @Test
  void getAllPostsByTagSuccessfully() {
    String tag = "tag";
    List<Post> posts = List.of(new Post(), new Post());
    when(postRepository.findAllByTag(tag)).thenReturn(posts);

    List<Post> result = postService.getAllPostsByTag(tag);

    assertEquals(posts, result);
    verify(postRepository, times(1)).findAllByTag(tag);
  }

}