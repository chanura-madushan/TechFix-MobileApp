package com.example.techfix.model;

public class User {
    public String userId;
    public String name;
    public String email;
    public String password;
    public String role;
    public String phone;

    public User() {}

    public User(String name, String email, String password, String role, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }
}