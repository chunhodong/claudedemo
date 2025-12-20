package com.example.claudedemo.service;

import com.example.claudedemo.dto.UserDto;
import com.example.claudedemo.entity.User;
import com.example.claudedemo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("전체 사용자 조회")
    void findAll() {
        User user1 = new User("홍길동", "hong@test.com", 30);
        User user2 = new User("김철수", "kim@test.com", 25);
        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2));

        List<User> users = userService.findAll();

        assertThat(users).hasSize(2);
        assertThat(users.get(0).getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("단건 사용자 조회 - 성공")
    void findById_success() {
        User user = new User("홍길동", "hong@test.com", 30);
        user.setId(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        User found = userService.findById(1L);

        assertThat(found.getName()).isEqualTo("홍길동");
        assertThat(found.getEmail()).isEqualTo("hong@test.com");
    }

    @Test
    @DisplayName("단건 사용자 조회 - 실패 (존재하지 않음)")
    void findById_notFound() {
        given(userRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("사용자 생성")
    void create() {
        UserDto dto = new UserDto(null, "홍길동", "hong@test.com", 30);
        User savedUser = new User("홍길동", "hong@test.com", 30);
        savedUser.setId(1L);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        User result = userService.create(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("사용자 수정")
    void update() {
        User existingUser = new User("홍길동", "hong@test.com", 30);
        existingUser.setId(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(existingUser));
        given(userRepository.save(any(User.class))).willReturn(existingUser);

        UserDto dto = new UserDto(null, "김철수", "kim@test.com", 25);
        User result = userService.update(1L, dto);

        assertThat(result.getName()).isEqualTo("김철수");
        assertThat(result.getEmail()).isEqualTo("kim@test.com");
        assertThat(result.getAge()).isEqualTo(25);
    }

    @Test
    @DisplayName("사용자 삭제")
    void delete() {
        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }
}
