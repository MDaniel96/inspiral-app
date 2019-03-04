package com.inspiral.inspiralbackend.models;

public class JwtTrainer {

    private Integer id;

    private String email;

    private String password;

    private String role;


    public JwtTrainer() {
        role = "admin";
    }

    public void setId(Integer id) { this.id = id; }

    public Integer getId() { return id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }
}
