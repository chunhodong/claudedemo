package com.example.claudedemo.controller;

import com.example.claudedemo.dto.UserDto;
import com.example.claudedemo.entity.User;
import com.example.claudedemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 사용자 조회")
    void getAll() {
        userRepository.save(new User("홍길동", "hong@test.com", 30));
        userRepository.save(new User("김철수", "kim@test.com", 25));

        webTestClient.get().uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class).hasSize(2);
    }

    @Test
    @DisplayName("단건 사용자 조회")
    void getById() {
        User saved = userRepository.save(new User("홍길동", "hong@test.com", 30));

        webTestClient.get().uri("/api/users/" + saved.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("홍길동")
                .jsonPath("$.email").isEqualTo("hong@test.com");
    }

    @Test
    @DisplayName("사용자 생성")
    void create() {
        UserDto dto = new UserDto(null, "홍길동", "hong@test.com", 30);

        webTestClient.post().uri("/api/users")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("홍길동")
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    @DisplayName("사용자 수정")
    void update() {
        User saved = userRepository.save(new User("홍길동", "hong@test.com", 30));
        UserDto dto = new UserDto(null, "김철수", "kim@test.com", 25);

        webTestClient.put().uri("/api/users/" + saved.getId())
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("김철수");
    }

    @Test
    @DisplayName("사용자 삭제")
    void deleteUser() {
        User saved = userRepository.save(new User("홍길동", "hong@test.com", 30));

        webTestClient.delete().uri("/api/users/" + saved.getId())
                .exchange()
                .expectStatus().isNoContent();
    }
}
