package com.example.claudedemo.user.domain;

public class User {

    private Long id;
    private String name;
    private String email;
    private Integer age;

    public User(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public static User create(String name, String email, Integer age) {
        return new User(null, name, email, age);
    }

    public void update(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
