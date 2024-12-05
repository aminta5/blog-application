package com.sc.demo.blogapplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sc.demo.blogapplication.dto.PostDTO;
import com.sc.demo.blogapplication.model.BlogUser;
import com.sc.demo.blogapplication.model.Post;
import com.sc.demo.blogapplication.repository.PostRepository;
import com.sc.demo.blogapplication.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock
  private PostRepository postRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private PostService postService;

  @Test
  void createPostSuccessfully() {
    Post post = new Post();
    BlogUser user = new BlogUser();
    PostDTO postDTO = new PostDTO("title", "content", UUID.randomUUID());
    when(postRepository.save(any(Post.class))).thenReturn(post);
    when(userRepository.findById(postDTO.userId())).thenReturn(Optional.of(user));

    Post result = postService.createPost(postDTO);

    assertEquals(post, result);
    verify(postRepository, times(1)).save(any(Post.class));
  }

  @Test
  void createPostUserNotFound() {
    PostDTO postDTO = new PostDTO("title", "content", UUID.randomUUID());
    when(userRepository.findById(postDTO.userId())).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> postService.createPost(postDTO));

    assertEquals("User not found", exception.getMessage());
    verify(postRepository, never()).save(any(Post.class));
  }

  @Test
  void getAllBlogPostsSuccessfully() {
    //create two BlogUser objects
    BlogUser user1 = new BlogUser();
    user1.setUsername("user1");
    BlogUser user2 = new BlogUser();
    user2.setUsername("user2");
    List<Post> posts = List.of(Post.builder().title("title").content("content").user(user1).build(),
        Post.builder().title("title1").content("content1").user(user2).build());
    when(postRepository.findAll()).thenReturn(posts);

    List<PostDTO> result = postService.getAllBlogPosts();

    assertEquals(posts.get(0).getContent(), result.get(0).content());
    assertEquals(posts.get(1).getContent(), result.get(1).content());
    verify(postRepository, times(1)).findAll();
  }

  @Test
  void updatePostSuccessfully() {
    UUID id = UUID.randomUUID();
    PostDTO updatedPost = new PostDTO("New Title", "New Content", UUID.randomUUID());
    Post post = new Post();
    BlogUser user = new BlogUser();
    user.setUsername("correctUser");
    post.setUser(user);
    when(postRepository.findById(id)).thenReturn(Optional.of(post));
    when(postRepository.save(post)).thenReturn(post);

    Optional<Post> result = postService.updatePost(id, updatedPost, "correctUser");

    assertTrue(result.isPresent());
    assertEquals(updatedPost.title(), result.get().getTitle());
    assertEquals(updatedPost.content(), result.get().getContent());
    verify(postRepository, times(1)).findById(id);
    verify(postRepository, times(1)).save(post);
  }

  @Test
  void updatePostUserMismatch() {
    UUID id = UUID.randomUUID();
    PostDTO updatedPost = new PostDTO("New Title", "New Content", UUID.randomUUID());
    Post post = new Post();
    BlogUser user = new BlogUser();
    user.setUsername("differentUser");
    post.setUser(user);
    when(postRepository.findById(id)).thenReturn(Optional.of(post));

    Optional<Post> result = postService.updatePost(id, updatedPost, "username");

    assertFalse(result.isPresent());
    verify(postRepository, times(1)).findById(id);
    verify(postRepository, never()).save(any(Post.class));
  }

  @Test
  void updatePostNotFound() {
    UUID id = UUID.randomUUID();
    PostDTO updatedPost = new PostDTO("New Title", "New Content", UUID.randomUUID());
    when(postRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    Optional<Post> result = postService.updatePost(id, updatedPost, "randomUsername");

    assertFalse(result.isPresent());
    verify(postRepository, times(1)).findById(id);
    verify(postRepository, never()).save(any(Post.class));
  }

  @Test
  void addTagToPostSuccessfully() {
    UUID id = UUID.randomUUID();
    String tag = "tag";
    Post post = new Post();
    when(postRepository.findById(id)).thenReturn(Optional.of(post));
    when(postRepository.save(post)).thenReturn(post);

    Optional<Post> result = postService.addTagToPost(id, tag);

    assertTrue(result.isPresent());
    assertTrue(result.get().getTags().contains(tag));
    verify(postRepository, times(1)).findById(id);
  }

  @Test
  void addTagToPostNotFound() {
    UUID id = UUID.randomUUID();
    String tag = "tag";
    when(postRepository.findById(id)).thenReturn(Optional.empty());

    Optional<Post> result = postService.addTagToPost(id, tag);

    assertFalse(result.isPresent());
    verify(postRepository, times(1)).findById(id);
  }

  @Test
  void removeTagFromPostSuccessfully() {
    UUID id = UUID.randomUUID();
    String tag = "tag";
    Post post = new Post();
    post.getTags().add(tag);
    when(postRepository.findById(id)).thenReturn(Optional.of(post));
    when(postRepository.save(post)).thenReturn(post);

    Optional<Post> result = postService.removeTagFromPost(id, tag);

    assertTrue(result.isPresent());
    assertFalse(result.get().getTags().contains(tag));
    verify(postRepository, times(1)).findById(id);
  }

  @Test
  void removeTagFromPostNotFound() {
    UUID id = UUID.randomUUID();
    String tag = "tag";
    when(postRepository.findById(id)).thenReturn(Optional.empty());

    Optional<Post> result = postService.removeTagFromPost(id, tag);

    assertFalse(result.isPresent());
    verify(postRepository, times(1)).findById(id);
  }

  @Test
  @SuppressWarnings("unchecked")
  void getAllPostsByTagSuccessfully() {
    String tag = "tag";
    List<Post> posts = List.of(new Post(), new Post());
    when(postRepository.findAll(any(Specification.class))).thenReturn(posts);

    List<Post> result = postService.getAllPostsByTag(tag);

    assertEquals(posts, result);
    verify(postRepository, times(1)).findAll(any(Specification.class));
  }

}