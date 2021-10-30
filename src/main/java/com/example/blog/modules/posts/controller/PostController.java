package com.example.blog.modules.posts.controller;

import com.example.blog.configs.security.CustomUserDetails;
import com.example.blog.modules.posts.exceptions.PostNotFoundException;
import com.example.blog.modules.posts.models.Post;
import com.example.blog.modules.posts.models.dto.PostInput;
import com.example.blog.modules.posts.models.dto.PostOutput;
import com.example.blog.modules.posts.services.PostService;
import com.example.blog.modules.users.exceptions.UserNotFoundException;
import com.example.blog.modules.users.models.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("/")
    public ResponseEntity<List<PostOutput>> getAllPosts() {
        List<PostOutput> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<PostOutput> createPost(@ModelAttribute @Valid PostInput postInput) throws UserNotFoundException, IOException {
        return new ResponseEntity<>(postService.createPost(postInput), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id) throws PostNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Post post = postService.getPostById(id);
        if (
                !userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Roles.Admin.name())) &&
                        !userDetails.getUsername().equals(post.getUser().getEmail())
        ) {
            throw new AccessDeniedException("Access Denied");
        }
        postService.deletePost(post);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostOutput> updatePost(@PathVariable(name = "id") Long id, @ModelAttribute @Valid PostInput postInput) throws PostNotFoundException, IOException, UserNotFoundException {
        return new ResponseEntity<>(postService.updatePost(id, postInput), HttpStatus.OK);
    }

}
