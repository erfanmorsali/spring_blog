package com.example.blog.modules.users.controller;

import com.example.blog.configs.security.JwtUtilClass;
import com.example.blog.modules.posts.exceptions.PostNotFoundException;
import com.example.blog.modules.users.exceptions.NotUniqueEmailException;
import com.example.blog.modules.users.exceptions.UserNotFoundException;
import com.example.blog.modules.users.models.dto.LoginInput;
import com.example.blog.modules.users.models.dto.UserInput;
import com.example.blog.modules.users.models.dto.UserOutput;
import com.example.blog.modules.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    JwtUtilClass jwtUtilClass;
    AuthenticationManager authenticationManager;
    UserDetailsService userDetailsService;
    UserService userService;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtUtilClass jwtUtilClass
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtilClass = jwtUtilClass;
    }

    @GetMapping("/")
    @PreAuthorize(value = "hasAuthority('Admin')")
    public ResponseEntity<List<UserOutput>> getAllUsers() throws FileNotFoundException {
        List<UserOutput> userOutputs = userService.getAllUsers();
        return new ResponseEntity<>(userOutputs, HttpStatus.OK);
    }

    @PostMapping(path = "/register/")
    public ResponseEntity<UserOutput> register(@Valid @ModelAttribute UserInput userIn) throws IOException, NotUniqueEmailException {
        UserOutput userOutput = userService.register(userIn);
        return new ResponseEntity<>(userOutput, HttpStatus.OK);
    }

    @PostMapping(path = "/login/")
    public ResponseEntity<Map<String, String>> Login(@Valid @RequestBody LoginInput loginInput) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginInput.getEmail(), loginInput.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginInput.getEmail());
        String token = jwtUtilClass.generateToken(userDetails);
        Map<String, String> response = new HashMap<>(1);
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{email}/")
    @PreAuthorize(value = "hasAuthority('Admin') or authentication.name == #email")
    public ResponseEntity<UserOutput> getUserByEmail(@PathVariable(name = "email") String email) throws UserNotFoundException, FileNotFoundException {
        UserOutput user = this.userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}/")
    @PreAuthorize(value = "hasAuthority('Admin') or authentication.name == #email")
    public ResponseEntity<String> deleteUserById(@PathVariable(name = "email") String email) throws PostNotFoundException {
        userService.deleteUserByEmail(email);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }
}
