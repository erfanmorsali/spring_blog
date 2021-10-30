package com.example.blog.modules.posts.repository;

import com.example.blog.modules.posts.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category getCategoryById(Long id);

    Set<Category> getCategoriesByIdIn(Set<Long> ids);
}
