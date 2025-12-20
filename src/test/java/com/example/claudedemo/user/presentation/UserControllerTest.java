package com.example.claudedemo.user.presentation;

import com.example.claudedemo.user.application.UserRequest;
import com.example.claudedemo.user.application.UserResponse;
import com.example.claudedemo.user.infrastructure.UserJpaEntity;
import com.example.claudedemo.user.infrastructure.UserJpaRepository;
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
    private UserJpaRepository userJpaRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
        userJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 사용자 조회")
    void getAll() {
        userJpaRepository.save(new UserJpaEntity(null, "홍길동", "hong@test.com", 30));
        userJpaRepository.save(new UserJpaEntity(null, "김철수", "kim@test.com", 25));

        webTestClient.get().uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class).hasSize(2);
    }

    @Test
    @DisplayName("단건 사용자 조회")
    void getById() {
        UserJpaEntity saved = userJpaRepository.save(new UserJpaEntity(null, "홍길동", "hong@test.com", 30));

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
        UserRequest request = new UserRequest(null, "홍길동", "hong@test.com", 30);

        webTestClient.post().uri("/api/users")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("홍길동")
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    @DisplayName("사용자 수정")
    void update() {
        UserJpaEntity saved = userJpaRepository.save(new UserJpaEntity(null, "홍길동", "hong@test.com", 30));
        UserRequest request = new UserRequest(null, "김철수", "kim@test.com", 25);

        webTestClient.put().uri("/api/users/" + saved.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("김철수");
    }

    @Test
    @DisplayName("사용자 삭제")
    void deleteUser() {
        UserJpaEntity saved = userJpaRepository.save(new UserJpaEntity(null, "홍길동", "hong@test.com", 30));

        webTestClient.delete().uri("/api/users/" + saved.getId())
                .exchange()
                .expectStatus().isNoContent();
    }
}
