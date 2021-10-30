package com.example.blog.modules.users.models.dto;

import javax.validation.constraints.NotNull;

public class LoginInput {

    @NotNull
    private String email;

    @NotNull
    private String password;

    public LoginInput(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginInput() {
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
}
