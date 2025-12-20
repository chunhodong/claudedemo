package com.example.claudedemo.product.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    boolean existsBySellerId(String sellerId);

    boolean existsBySellerIdAndIdNot(String sellerId, Long id);
}
