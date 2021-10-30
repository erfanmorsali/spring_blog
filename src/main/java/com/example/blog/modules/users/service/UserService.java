package com.example.blog.modules.users.service;

import com.example.blog.modules.posts.exceptions.PostNotFoundException;
import com.example.blog.modules.users.exceptions.NotUniqueEmailException;
import com.example.blog.modules.users.exceptions.UserNotFoundException;
import com.example.blog.modules.users.models.User;
import com.example.blog.modules.users.models.dto.UserInput;
import com.example.blog.modules.users.models.dto.UserOutput;
import com.example.blog.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserOutput getUserByEmail(String email) throws UserNotFoundException, FileNotFoundException {
        User user = this.userRepository.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User Not Found With Email : " + email);
        }
        return user.toUserOutput();
    }

    public List<UserOutput> getAllUsers() throws FileNotFoundException {
        return userRepository.findAll().stream().map(user -> {
            try {
                return user.toUserOutput();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }


    public UserOutput register(UserInput userInput) throws IOException, NotUniqueEmailException {
        if (this.userRepository.existsByEmail(userInput.getEmail())) {
            throw new NotUniqueEmailException("Email Is Already Taken");
        }
        User user = userInput.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        return user.toUserOutput();
    }

    @Transactional
    public void deleteUserByEmail(String email) throws PostNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new PostNotFoundException("User With Email : " + email + " Not Found");
        }
        user.removePosts();
        userRepository.deleteByEmail(email);
    }
}
