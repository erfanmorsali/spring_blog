package com.example.blog.modules.users.models;


import com.example.blog.modules.posts.models.Post;
import com.example.blog.modules.users.models.dto.UserOutput;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.ResourceUtils;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    private String cover;


    private String roles;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true
    )
    private Set<Post> posts = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isEnabled = true;

    public User() {
    }

    public User(String email, String password, String name, String cover, Roles roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.cover = cover;
        this.roles = roles.name();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void removePosts() {
        Iterator<Post> iterator = this.posts.iterator();
        while (iterator.hasNext()) {
            Post post = iterator.next();
            post.removeCategories();
            post.setUser(null);
            iterator.remove();
        }
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public String getUserCoverPath() throws FileNotFoundException {
        if (this.cover == null) {
            return null;
        } else {
            String folderPath = ResourceUtils.getFile("classpath:static/img/").getAbsolutePath();
            return Paths.get(folderPath + File.separator + this.cover).toString();
        }
    }

    public UserOutput toUserOutput() throws FileNotFoundException {
        return new UserOutput(name, email, getUserCoverPath(), posts);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
