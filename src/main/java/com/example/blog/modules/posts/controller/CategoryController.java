package com.example.blog.modules.posts.controller;


import com.example.blog.modules.posts.exceptions.CategoryNotFoundException;
import com.example.blog.modules.posts.models.dto.CategoryInput;
import com.example.blog.modules.posts.models.dto.CategoryOutput;
import com.example.blog.modules.posts.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryOutput>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryOutput> createCategory(@RequestBody CategoryInput categoryInput) {
        return new ResponseEntity<>(categoryService.createCategory(categoryInput), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable(name = "id") Long id, @RequestBody CategoryInput categoryInput) throws CategoryNotFoundException {
        categoryService.updateCategory(id, categoryInput);
        return new ResponseEntity<>("updated", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id") Long id) throws CategoryNotFoundException {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}
