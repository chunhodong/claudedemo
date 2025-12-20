package com.example.claudedemo.service;

import com.example.claudedemo.dto.UserDto;
import com.example.claudedemo.entity.User;
import com.example.claudedemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public User create(UserDto dto) {
        User user = new User(dto.getName(), dto.getEmail(), dto.getAge());
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, UserDto dto) {
        User user = findById(id);
        user.update(dto.getName(), dto.getEmail(), dto.getAge());
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
