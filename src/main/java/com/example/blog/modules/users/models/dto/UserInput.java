package com.example.blog.modules.users.models.dto;

import com.example.blog.modules.users.models.Roles;
import com.example.blog.modules.users.models.User;
import com.example.blog.modules.users.validators.EmailFormatConstraint;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class UserInput {
    @NotNull(message = "name cant be null")
    @Size(min = 2, message = "name cant be less than 2 char")
    private String name;

    @EmailFormatConstraint(message = "invalid email")
    private String email;

    @NotNull
    @Size(min = 8, message = "password must at least have 8 char")
    private String password;


    private MultipartFile cover;

    public UserInput() {
    }

    public UserInput(String name, String email, String password, MultipartFile cover) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MultipartFile getCover() {
        return cover;
    }

    public void setCover(MultipartFile cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "UserInput{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }

    public String saveCover() throws IOException {
        String coverAddress = null;
        String folderPath = ResourceUtils.getFile("classpath:static/img/").getAbsolutePath();
        if (this.cover != null) {
            byte[] fileBytes = this.cover.getBytes();
            String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(this.cover.getContentType()).split("/")[1];
            Path filePath = Paths.get(folderPath + File.separator + fileName);
            Files.write(filePath, fileBytes);
            coverAddress = fileName;
        }
        return coverAddress;
    }

    public User toUser() throws IOException {
        String cover = saveCover();
        return new User(
                email, password, name, cover, Roles.User
        );
    }
}
