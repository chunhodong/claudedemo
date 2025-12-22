package com.example.claudedemo.fee.domain;

import java.util.List;
import java.util.Optional;

public interface FeeRepository {
    Fee save(Fee fee);
    Optional<Fee> findById(Long id);
    List<Fee> findAll();
    void deleteById(Long id);
}
