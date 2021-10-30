package com.example.blog.modules.posts.models.dto;

public class CategoryInput {
    private String name;

    public CategoryInput() {

    }

    public CategoryInput(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryInput{" +
                "name='" + name + '\'' +
                '}';
    }
}
