package com.example.blog.modules.posts.services;

import com.example.blog.modules.posts.exceptions.PostNotFoundException;
import com.example.blog.modules.posts.models.Category;
import com.example.blog.modules.posts.models.Post;
import com.example.blog.modules.posts.models.dto.PostInput;
import com.example.blog.modules.posts.models.dto.PostOutput;
import com.example.blog.modules.posts.repository.CategoryRepository;
import com.example.blog.modules.posts.repository.PostRepository;
import com.example.blog.modules.users.exceptions.UserNotFoundException;
import com.example.blog.modules.users.models.Roles;
import com.example.blog.modules.users.models.User;
import com.example.blog.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    PostRepository postRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<PostOutput> getAllPosts() {
        System.out.println("service");
        return postRepository.findAll().stream().map(post -> {
            try {
                return post.toPostOutput();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    public PostOutput createPost(PostInput postInput, String email) throws IOException, UserNotFoundException {
        User postUser = userRepository.getUserByEmail(email);
        if (postUser == null) {
            throw new UserNotFoundException("User Not Found");
        }
        Set<Category> categories = categoryRepository.getCategoriesByIdIn(postInput.getCategories());
        Post post = postInput.toPost();
        post.setUser(postUser);
        categories.forEach(post::addCategory);
        Post newPost = postRepository.save(post);
        return newPost.toPostOutput();
    }

    public void deletePost(Post post) throws PostNotFoundException {
        post.removeCategories();
        postRepository.delete(post);
    }

    public Post getPostById(Long id) throws PostNotFoundException {
        Post post = postRepository.getPostById(id);
        if (post == null) {
            throw new PostNotFoundException("Post With Id : " + id + " Not Found");
        }
        return post;
    }

    public boolean userCanDeletePost(Authentication authentication, Post post) {
        String email = authentication.getName();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.Admin.name())) ||
                email.equals(post.getUser().getEmail());
    }

    public PostOutput updatePost(Long id, PostInput postUpdateInput, String email) throws
            PostNotFoundException, IOException, UserNotFoundException {
        Post post = getPostById(id);
        User postUser = userRepository.getUserByEmail(email);
        if (postUser == null) {
            throw new UserNotFoundException("User Not Found");
        }
        Post newPostInfo = postUpdateInput.toPost();
        post.setUser(postUser);
        post.setDesc(newPostInfo.getDescription());
        post.setTitle(newPostInfo.getTitle());
        if (newPostInfo.getPostImage() != null) {
            post.setImage(newPostInfo.getPostImage());
        }
        Set<Category> categories = categoryRepository.getCategoriesByIdIn(postUpdateInput.getCategories());
        post.removeCategories();
        categories.forEach(post::addCategory);
        postRepository.save(post);
        return post.toPostOutput();
    }

}
