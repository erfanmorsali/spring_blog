package com.example.blog.modules.posts.models;


import com.example.blog.modules.posts.models.dto.PostOutput;
import com.example.blog.modules.users.models.User;
import org.springframework.util.ResourceUtils;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {


    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 200)
    private String description;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private User user;


    @ManyToMany(mappedBy = "posts",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    private Set<Category> categories = new HashSet<>();

    public Post() {
    }

    public Post(String title, String description, String image, User user, Set<Category> categories) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.user = user;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setDesc(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        category.getPosts().add(this);
        categories.add(category);
    }

    public void removeCategory(Category category) {
        category.getPosts().remove(this);
        categories.remove(category);
    }

    public void removeCategories() {
        for (Category category : this.categories) {
            category.getPosts().remove(this);
        }
        categories = new HashSet<>();
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getPostImage() throws FileNotFoundException {
        if (image == null) {
            return null;
        } else {
            String folderPath = ResourceUtils.getFile("classpath:static/img/").getAbsolutePath();
            return Paths.get(folderPath + File.separator + image).toString();
        }
    }

    public PostOutput toPostOutput() throws FileNotFoundException {
        String image = getPostImage();
        return new PostOutput(title, description, image, user, categories);
    }
}
