package com.example.blog.modules.posts.models.dto;

import com.example.blog.modules.posts.models.Category;
import com.example.blog.modules.users.models.User;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostOutput {
    private String title;
    private String description;
    private String image;
    private PostUserDto user;
    private Set<PostCategoryDto> categories = new HashSet<>();

    public PostOutput() {
    }

    public PostOutput(String title, String description, String image, User user, Set<Category> categories) throws FileNotFoundException {
        this.title = title;
        this.description = description;
        this.image = image;
        this.user = toPostUserDto(user);
        this.categories = toPostCategoryDto(categories);
    }


    public void setCategories(Set<PostCategoryDto> categories) {
        this.categories = categories;
    }

    public void setUser(PostUserDto user) {
        this.user = user;
    }

    public Set<PostCategoryDto> getCategories() {
        return categories;
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

    public PostUserDto getUser() {
        return user;
    }

    public void setUserOutput(User user) throws FileNotFoundException {
        this.user = toPostUserDto(user);
    }

    @Override
    public String toString() {
        return "PostOutput{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public PostUserDto toPostUserDto(User user) throws FileNotFoundException {
        if (user == null) {
            return null;
        }
        return new PostUserDto(user.getName(), user.getEmail(), user.getUserCoverPath());
    }

    public Set<PostCategoryDto> toPostCategoryDto(Set<Category> categories) {
        return categories.stream().map(category -> new PostCategoryDto(category.getName())).collect(Collectors.toSet());
    }

    private static class PostUserDto {
        private String name;
        private String email;
        private String cover;

        public PostUserDto() {

        }

        public PostUserDto(String name, String email, String cover) {
            this.name = name;
            this.email = email;
            this.cover = cover;
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

        @Override
        public String toString() {
            return "PostUserDto{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", cover='" + cover + '\'' +
                    '}';
        }
    }

    static class PostCategoryDto {
        private String name;

        public PostCategoryDto() {
        }

        public PostCategoryDto(String name) {
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "PostCategoryDto{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
