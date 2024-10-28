package com.sc.demo.blogapplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @Test
  void testCreatePostSuccess() throws Exception {
    PostDTO postDto = new PostDTO("title", "content");
    Post post = Post.builder().title(postDto.title()).content(postDto.content()).build();

    mockMvc.perform(post("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(post)))
        .andExpect(status().isOk());

  }

  @Test
  void testCreatePostFailure() throws Exception {
    Post post = Post.builder().title("title").content("").build();
    given(postService.createPost(post)).willThrow(
        new ConstraintViolationException("No Content", null));
    mockMvc.perform(post("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(post)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetAllPostsSuccess() throws Exception {
    List<PostDTO> posts = List.of(new PostDTO("title", "content"));
    given(postService.getAllBlogPosts()).willReturn(posts);
    mockMvc.perform(get("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

  }

  @Test
  void testUpdatePostSuccess() throws Exception {
    PostDTO postDto = new PostDTO("new title", "new content");
    Post updatedPost = Post.builder().title(postDto.title()).content(postDto.content()).build();
    given(postService.updatePost(anyLong(), any(PostDTO.class))).willReturn(
        Optional.of(updatedPost));

    mockMvc.perform(put("/api/v1/posts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(postDto)))
        .andExpect(status().isOk());
  }

  @Test
  void testUpdatePostNotFound() throws Exception {
    PostDTO postDto = new PostDTO("new title", "new content");
    given(postService.updatePost(anyLong(), any(PostDTO.class))).willReturn(Optional.empty());

    mockMvc.perform(put("/api/v1/posts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(postDto)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testAddTagToPostSuccess() throws Exception {
    String tag = "tag";
    given(postService.addTagToPost(anyLong(), anyString())).willReturn(
        Optional.of(Post.builder().title("title").content("content").tags(
            Set.of(tag)).build()));

    mockMvc.perform(patch("/api/v1/posts/1/tags?tag=" + tag + "&add=true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void testAddTagToPostNotFound() throws Exception {
    String tag = "tag";
    given(postService.addTagToPost(anyLong(), anyString())).willReturn(Optional.empty());

    mockMvc.perform(patch("/api/v1/posts/1/tags?tag=" + tag + "&add=true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void testRemoveTagFromPostSuccess() throws Exception {
    String tag = "tag";
    given(postService.removeTagFromPost(anyLong(), anyString())).willReturn(Optional.of(
        Post.builder().title("title").content("content").tags(new HashSet<>()).build()));

    mockMvc.perform(patch("/api/v1/posts/1/tags?tag=" + tag + "&add=false")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void testRemoveTagFromPostNotFound() throws Exception {
    String tag = "tag";
    given(postService.removeTagFromPost(anyLong(), anyString())).willReturn(Optional.empty());

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
