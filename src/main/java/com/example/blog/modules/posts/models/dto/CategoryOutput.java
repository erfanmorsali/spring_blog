package com.example.blog.modules.posts.models.dto;

import com.example.blog.modules.posts.models.Category;
import com.example.blog.modules.posts.models.Post;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryOutput {
    private String name;
    private Set<CategoryPostOutput> posts;


    public CategoryOutput() {
    }

    public CategoryOutput(String name, Set<Post> posts) {
        this.name = name;
        this.posts = toCategoryPostOutput(posts);
    }

    public void setPosts(Set<CategoryPostOutput> posts) {
        this.posts = posts;
    }

    public Set<CategoryPostOutput> getPosts() {
        return posts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CategoryPostOutput> toCategoryPostOutput(Set<Post> posts) {
        return posts.stream().map(post -> {
            try {
                return new CategoryPostOutput(post.getTitle(), post.getDescription(), post.getPostImage());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "CategoryOutput{" +
                "name='" + name + '\'' +
                '}';
    }


    static class CategoryPostOutput {
        private String title;
        private String description;
        private String image;


        public CategoryPostOutput() {
        }

        public CategoryPostOutput(String title, String description, String image) {
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

        @Override
        public String toString() {
            return "CategoryPostOutput{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }
}
