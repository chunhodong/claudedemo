package com.example.claudedemo.user.application;

import com.example.claudedemo.user.domain.DuplicateEmailException;
import com.example.claudedemo.user.domain.User;
import com.example.claudedemo.user.domain.UserNotFoundException;
import com.example.claudedemo.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("findAll 메소드는")
    class Describe_findAll {

        @Nested
        @DisplayName("사용자가 존재하면")
        class Context_with_users {

            @Test
            @DisplayName("전체 사용자 목록을 반환한다")
            void it_returns_all_users() {
                // given
                User user1 = new User(1L, "홍길동", "hong@test.com", 30);
                User user2 = new User(2L, "김철수", "kim@test.com", 25);
                given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2));

                // when
                List<UserResponse> result = userService.findAll();

                // then
                assertThat(result).hasSize(2);
                assertThat(result.get(0).getName()).isEqualTo("홍길동");
                assertThat(result.get(1).getName()).isEqualTo("김철수");
            }
        }
    }

    @Nested
    @DisplayName("findById 메소드는")
    class Describe_findById {

        @Nested
        @DisplayName("존재하는 ID로 조회하면")
        class Context_with_valid_id {

            @Test
            @DisplayName("해당 사용자를 반환한다")
            void it_returns_user() {
                // given
                User user = new User(1L, "홍길동", "hong@test.com", 30);
                given(userRepository.findById(1L)).willReturn(Optional.of(user));

                // when
                UserResponse result = userService.findById(1L);

                // then
                assertThat(result.getName()).isEqualTo("홍길동");
                assertThat(result.getEmail()).isEqualTo("hong@test.com");
            }
        }

        @Nested
        @DisplayName("존재하지 않는 ID로 조회하면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("UserNotFoundException을 던진다")
            void it_throws_exception() {
                // given
                given(userRepository.findById(999L)).willReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> userService.findById(999L))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("중복되지 않은 이메일로 생성하면")
        class Context_with_unique_email {

            @Test
            @DisplayName("새 사용자를 생성하고 반환한다")
            void it_creates_user() {
                // given
                UserRequest request = new UserRequest(null, "홍길동", "hong@test.com", 30);
                User savedUser = new User(1L, "홍길동", "hong@test.com", 30);
                given(userRepository.existsByEmail("hong@test.com")).willReturn(false);
                given(userRepository.save(any(User.class))).willReturn(savedUser);

                // when
                UserResponse result = userService.create(request);

                // then
                assertThat(result.getId()).isEqualTo(1L);
                assertThat(result.getName()).isEqualTo("홍길동");
            }
        }

        @Nested
        @DisplayName("중복된 이메일로 생성하면")
        class Context_with_duplicate_email {

            @Test
            @DisplayName("DuplicateEmailException을 던진다")
            void it_throws_exception() {
                // given
                UserRequest request = new UserRequest(null, "홍길동", "hong@test.com", 30);
                given(userRepository.existsByEmail("hong@test.com")).willReturn(true);

                // when & then
                assertThatThrownBy(() -> userService.create(request))
                        .isInstanceOf(DuplicateEmailException.class)
                        .hasMessageContaining("Email already exists");
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("존재하는 사용자를 수정하면")
        class Context_with_valid_user {

            @Test
            @DisplayName("수정된 사용자 정보를 반환한다")
            void it_updates_user() {
                // given
                User existingUser = new User(1L, "홍길동", "hong@test.com", 30);
                User updatedUser = new User(1L, "김철수", "kim@test.com", 25);
                given(userRepository.findById(1L)).willReturn(Optional.of(existingUser));
                given(userRepository.save(any(User.class))).willReturn(updatedUser);

                UserRequest request = new UserRequest(null, "김철수", "kim@test.com", 25);

                // when
                UserResponse result = userService.update(1L, request);

                // then
                assertThat(result.getName()).isEqualTo("김철수");
                assertThat(result.getEmail()).isEqualTo("kim@test.com");
                assertThat(result.getAge()).isEqualTo(25);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("ID로 삭제하면")
        class Context_with_id {

            @Test
            @DisplayName("해당 사용자를 삭제한다")
            void it_deletes_user() {
                // given
                Long userId = 1L;

                // when
                userService.delete(userId);

                // then
                then(userRepository).should().deleteById(userId);
            }
        }
    }
}
