package com.example.claudeapi.fee.infrastructure;

import com.example.claudeapi.fee.domain.Fee;
import com.example.claudeapi.fee.domain.FeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FeeRepositoryImpl implements FeeRepository {

    private final FeeJpaRepository jpaRepository;

    @Override
    public Fee save(Fee fee) {
        FeeJpaEntity entity = FeeJpaEntity.from(fee);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Fee> findById(Long id) {
        return jpaRepository.findById(id).map(FeeJpaEntity::toDomain);
    }

    @Override
    public List<Fee> findAll() {
        return jpaRepository.findAll().stream()
                .map(FeeJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
