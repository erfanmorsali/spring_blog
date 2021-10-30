package com.example.blog.modules.users.repository;

import com.example.blog.modules.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);

    Boolean existsByEmail(String email);

    void deleteByEmail(String email);

    User getUserById(Long id);
}
