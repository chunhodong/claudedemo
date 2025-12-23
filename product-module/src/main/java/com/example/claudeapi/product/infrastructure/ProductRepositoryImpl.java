package com.example.claudeapi.product.infrastructure;

import com.example.claudeapi.product.domain.Product;
import com.example.claudeapi.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = ProductJpaEntity.from(product);
        ProductJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ProductJpaEntity::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsBySellerId(String sellerId) {
        return jpaRepository.existsBySellerId(sellerId);
    }

    @Override
    public boolean existsBySellerIdAndIdNot(String sellerId, Long id) {
        return jpaRepository.existsBySellerIdAndIdNot(sellerId, id);
    }
}
