package com.example.claudeapi.product.infrastructure;

import com.example.claudeapi.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sellerId;

    private Long categoryNumber;

    public ProductJpaEntity(Long id, String name, String sellerId, Long categoryNumber) {
        this.id = id;
        this.name = name;
        this.sellerId = sellerId;
        this.categoryNumber = categoryNumber;
    }

    public static ProductJpaEntity from(Product product) {
        return new ProductJpaEntity(product.getId(), product.getName(), product.getSellerId(), product.getCategoryNumber());
    }

    public Product toDomain() {
        return new Product(id, name, sellerId, categoryNumber);
    }

    public void update(String name, String sellerId, Long categoryNumber) {
        this.name = name;
        this.sellerId = sellerId;
        this.categoryNumber = categoryNumber;
    }
}
