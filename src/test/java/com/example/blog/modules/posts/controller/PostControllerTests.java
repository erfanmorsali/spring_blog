package com.example.blog.modules.posts.controller;


import com.example.blog.modules.posts.exceptions.PostNotFoundException;
import com.example.blog.modules.posts.models.Post;
import com.example.blog.modules.posts.models.dto.PostInput;
import com.example.blog.modules.posts.models.dto.PostOutput;
import com.example.blog.modules.posts.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc()
public class PostControllerTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PostService postService;


    @Test
    public void getAllPostsWithUnauthenticatedUserFails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/posts/");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Assertions.assertEquals(mvcResult.getResponse().getStatus(), 403);
    }

    @WithMockUser("admin")
    @Test
    public void getAllPostsWithAuthenticatedUserSuccess() throws Exception {
        List<PostOutput> postOutputList = new ArrayList<>();
        Mockito.when(postService.getAllPosts()).thenReturn(postOutputList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/posts/");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
        ;
    }

    @Test
    public void createPostWithUnauthenticatedUserFails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts/create")
                .param("title", "fake title")
                .param("description", "fake desc");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser
    @Test
    public void createPostWithAuthenticatedUserSuccess() throws Exception {
        PostInput postInput = new PostInput("fake title", "fake description", null, null);
        Mockito.when(postService.createPost(Mockito.any(PostInput.class), Mockito.anyString())).thenReturn(new PostOutput());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts/create")
                .param("title", postInput.getTitle())
                .param("description", postInput.getDescription());
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
        ;
    }

    @WithMockUser
    @Test
    public void createPostWithInvalidDataFails() throws Exception {
        Mockito.when(postService.createPost(Mockito.any(PostInput.class), Mockito.anyString())).thenReturn(new PostOutput());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts/create")
                .param("description", "fake desc");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
        ;
    }

    @Test
    public void deletePostWithUnauthenticatedUserFails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/posts/delete/5");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser
    @Test
    public void deletePostWithAuthenticatedUserSuccess() throws Exception {
        Mockito.when(postService.getPostById(Mockito.anyLong())).thenReturn(new Post());
        Mockito.when(postService.userCanDeletePost(Mockito.any(Authentication.class), Mockito.any(Post.class))).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/posts/delete/5");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""))
        ;
    }

    @WithMockUser
    @Test
    public void deletePostWithInvalidIdThrowsPostNotFoundException() throws Exception {
        Mockito.when(postService.getPostById(Mockito.anyLong())).thenThrow(PostNotFoundException.class);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/posts/delete/5");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @WithMockUser
    @Test
    public void deleteAnotherUserPostMustFail() throws Exception {
        Mockito.when(postService.getPostById(Mockito.anyLong())).thenReturn(new Post());
        Mockito.when(postService.userCanDeletePost(Mockito.any(Authentication.class), Mockito.any(Post.class))).thenReturn(false);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/posts/delete/5");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden())
        ;
    }

    @Test
    public void updatePostWithUnauthenticatedUserFails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/posts/update/5");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser
    @Test
    public void updatePostWithAuthenticatedUserSuccess() throws Exception {
        Mockito.when(postService.updatePost(Mockito.anyLong(), Mockito.any(PostInput.class), Mockito.anyString())).thenReturn(new PostOutput());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/posts/update/5")
                .param("title", "fake title")
                .param("description", "fake description");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser
    @Test
    public void updatePostWithInvalidDataFails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/posts/update/5")
                .param("title", "fake title")
                .param("description", "fake desc");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}


