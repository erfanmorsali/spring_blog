package com.example.blog.modules.users.models.dto;

import com.example.blog.modules.posts.models.Post;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserOutput {
    private String name;
    private String email;
    private String cover;
    private Set<UserPostDto> posts = new HashSet<>();

    public UserOutput() {
    }

    public UserOutput(String name, String email, String cover, Set<Post> posts) {
        this.name = name;
        this.email = email;
        this.cover = cover;
        this.posts = toUserPostDto(posts);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Set<UserPostDto> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = toUserPostDto(posts);
    }

    @Override
    public String toString() {
        return "UserOutput{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }

    public Set<UserPostDto> toUserPostDto(Set<Post> posts) {
        return posts.stream().map(post -> {
                    try {
                        return new UserPostDto(
                                post.getTitle(),
                                post.getDescription(),
                                post.getPostImage());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toSet());
    }

    private static class UserPostDto {
        private String title;
        private String description;
        private String image;

        public UserPostDto() {
        }

        public UserPostDto(String title, String description, String image) {
            this.title = title;
            this.description = description;
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
