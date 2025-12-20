package com.example.claudedemo.user.application;

import com.example.claudedemo.user.domain.User;
import com.example.claudedemo.user.domain.UserNotFoundException;
import com.example.claudedemo.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        User user = User.create(request.getName(), request.getEmail(), request.getAge());
        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.update(request.getName(), request.getEmail(), request.getAge());
        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
