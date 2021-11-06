package com.example.blog.modules.posts.models.dto;

import com.example.blog.modules.posts.models.Post;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PostInput {
    @NotNull
    @Size(min = 5, max = 100, message = "length must be between 5 to 100")
    private String title;

    @NotNull
    @Size(min = 10, max = 200, message = "length must be between 10 to 200")
    private String description;

    private MultipartFile image;


    @NotNull(message = "categories cant be null")
    private Set<Long> categories = new HashSet<>();

    public PostInput() {
    }

    public PostInput(String title, String description, MultipartFile image, Set<Long> categories) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.categories = categories;
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public void setCategories(Set<Long> categories) {
        this.categories = categories;
    }

    public Set<Long> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "PostInput{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                '}';
    }

    public String saveImage() throws IOException {
        String imageAddress = null;
        String folderPath = ResourceUtils.getFile("classpath:static/img/").getAbsolutePath();
        if (image != null) {
            byte[] fileBytes = image.getBytes();
            String fileName = UUID.randomUUID() + "." + Objects.requireNonNull(image.getContentType()).split("/")[1];
            Path filePath = Paths.get(folderPath + File.separator + fileName);
            Files.write(filePath, fileBytes);
            imageAddress = fileName;
        }
        return imageAddress;
    }


    public Post toPost() throws IOException {
        String imageAddress = this.saveImage();
        return new Post(title, description, imageAddress, null, new HashSet<>());
    }
}
