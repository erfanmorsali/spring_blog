package com.example.blog.modules.posts.services;


import com.example.blog.modules.posts.exceptions.CategoryNotFoundException;
import com.example.blog.modules.posts.models.Category;
import com.example.blog.modules.posts.models.dto.CategoryInput;
import com.example.blog.modules.posts.models.dto.CategoryOutput;
import com.example.blog.modules.posts.repository.CategoryRepository;
import com.example.blog.modules.users.models.dto.UserOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    CategoryRepository categoryRepository;


    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryOutput> getAllCategories() {
        return categoryRepository.findAll().stream().map(Category::toCategoryOutput).collect(Collectors.toList());
    }

    public CategoryOutput createCategory(CategoryInput categoryInput) {
        Category category = new Category(categoryInput.getName(), new HashSet<>());
        Category newCategory = categoryRepository.save(category);
        return new CategoryOutput(newCategory.getName(), newCategory.getPosts());
    }

    public void deleteCategory(Long id) throws CategoryNotFoundException {
        boolean categoryExists = categoryRepository.existsById(id);
        if (!categoryExists) {
            throw new CategoryNotFoundException("Category With Id : " + id + " Not Found");
        }
        categoryRepository.deleteById(id);
    }

    public void updateCategory(Long id, CategoryInput categoryInput) throws CategoryNotFoundException {
        Category category = categoryRepository.getCategoryById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Category With Id : " + id + " Not Found");
        }

        category.setName(categoryInput.getName());
        categoryRepository.save(category);
    }
}
