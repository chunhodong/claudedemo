package com.example.claudeapi.product.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);

    boolean existsBySellerId(String sellerId);

    boolean existsBySellerIdAndIdNot(String sellerId, Long id);
}
