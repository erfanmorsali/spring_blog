package com.example.blog.modules.posts.repository;

import com.example.blog.modules.posts.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post getPostById(Long id);
    boolean existsById(Long id);
}
