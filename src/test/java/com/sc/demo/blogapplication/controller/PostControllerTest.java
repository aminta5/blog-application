/*
package com.sc.demo.blogapplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.demo.blogapplication.dto.PostDTO;
import com.sc.demo.blogapplication.model.Post;
import com.sc.demo.blogapplication.service.PostService;
import com.sc.demo.blogapplication.service.TokenBlacklistService;
import com.sc.demo.blogapplication.util.JwtUtil;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
@ActiveProfiles("test")
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JwtUtil jwtUtil;

  @MockBean
  private TokenBlacklistService tokenBlacklistService;

  @MockBean
  private PostService postService;

  @Test
  void testCreatePostSuccess() throws Exception {
    PostDTO postDTO = new PostDTO("title", "content", UUID.randomUUID());
    Post post = Post.builder().title(postDTO.title()).content(postDTO.content()).build();

    String token = "mocked-jwt-token";
    given(jwtUtil.validateToken(anyString(), anyString())).willReturn(true);
    given(tokenBlacklistService.isTokenBlacklisted(anyString())).willReturn(false);

    mockMvc.perform(post("/api/v1/posts")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(post)))
        .andExpect(status().isOk());

  }

  @Test
  void testCreatePostFailure() throws Exception {
    PostDTO postDTO = new PostDTO("title", "content", UUID.randomUUID());
    given(postService.createPost(postDTO)).willThrow(
        new ConstraintViolationException("No Content", null));
    mockMvc.perform(post("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(postDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetAllPostsSuccess() throws Exception {
    PostDTO postDTO1 = new PostDTO("title1", "content1", UUID.randomUUID());
    PostDTO postDTO2 = new PostDTO("title2", "content2", UUID.randomUUID());
    List<PostDTO> posts = List.of(postDTO1, postDTO2);
    given(postService.getAllBlogPosts()).willReturn(posts);
    mockMvc.perform(get("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

  }

  @Test
  void testUpdatePostSuccess() throws Exception {
    PostDTO postDTO = new PostDTO("new title", "new content", UUID.randomUUID());
    Post updatedPost = Post.builder().title(postDTO.title()).content(postDTO.content()).build();
    given(postService.updatePost(any(UUID.class), any(PostDTO.class), anyString())).willReturn(
        Optional.of(updatedPost));

    mockMvc.perform(put("/api/v1/posts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(postDTO)))
        .andExpect(status().isOk());
  }

  @Test
  void testUpdatePostNotFound() throws Exception {
    PostDTO postDTO = new PostDTO("new title", "new content", UUID.randomUUID());
    given(postService.updatePost(any(UUID.class), any(PostDTO.class), anyString())).willReturn(
        Optional.empty());

    mockMvc.perform(put("/api/v1/posts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(postDTO)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testAddTagToPostSuccess() throws Exception {
    String tag = "tag";
    given(postService.addTagToPost(any(UUID.class), anyString())).willReturn(
        Optional.of(Post.builder().title("title").content("content").tags(
            Set.of(tag)).build()));

    mockMvc.perform(patch("/api/v1/posts/1/tags?tag=" + tag + "&add=true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void testAddTagToPostNotFound() throws Exception {
    String tag = "tag";
    given(postService.addTagToPost(any(UUID.class), anyString())).willReturn(Optional.empty());

    mockMvc.perform(patch("/api/v1/posts/1/tags?tag=" + tag + "&add=true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void testRemoveTagFromPostSuccess() throws Exception {
    String tag = "tag";
    given(postService.removeTagFromPost(any(UUID.class), anyString())).willReturn(Optional.of(
        Post.builder().title("title").content("content").tags(new HashSet<>()).build()));

    mockMvc.perform(patch("/api/v1/posts/1/tags?tag=" + tag + "&add=false")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void testRemoveTagFromPostNotFound() throws Exception {
    String tag = "tag";
    given(postService.removeTagFromPost(any(UUID.class), anyString())).willReturn(Optional.empty());

    mockMvc.perform(patch("/api/v1/posts/1/tags?tag=" + tag + "&add=false")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetPostsByTagSuccess() throws Exception {
    List<Post> posts = List.of(Post.builder()
        .title("title")
        .content("content")
        .tags(Set.of("tag")).build());
    given(postService.getAllPostsByTag(anyString())).willReturn(posts);
    mockMvc.perform(get("/api/v1/posts").param("tag", "tag")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void testGetPostsByTagNotFound() throws Exception {
    given(postService.getAllPostsByTag(anyString())).willReturn(List.of());
    mockMvc.perform(get("/api/v1/posts").param("tag", "not-exist-tag")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
*/
