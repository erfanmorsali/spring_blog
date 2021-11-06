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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        String username = principal.getName();
        return new ResponseEntity<>(postService.createPost(postInput, username), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id) throws PostNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Post post = postService.getPostById(id);
        if (postService.userCanDeletePost(authentication, post)) {
            postService.deletePost(post);
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }

        throw new AccessDeniedException("access denied");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostOutput> updatePost(
            @PathVariable(name = "id") Long id,
            @ModelAttribute @Valid PostInput postInput

    ) throws PostNotFoundException, IOException, UserNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(postService.updatePost(id, postInput, email), HttpStatus.OK);
    }

}
