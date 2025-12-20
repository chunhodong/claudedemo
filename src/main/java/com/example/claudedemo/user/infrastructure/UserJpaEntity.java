package com.example.claudedemo.user.infrastructure;

import com.example.claudedemo.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private Integer age;

    public UserJpaEntity(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public static UserJpaEntity from(User user) {
        return new UserJpaEntity(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }

    public User toDomain() {
        return new User(id, name, email, age);
    }

    public void update(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
