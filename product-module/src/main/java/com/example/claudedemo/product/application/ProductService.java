package com.example.claudedemo.product.application;

import com.example.claudedemo.product.domain.DuplicateSellerIdException;
import com.example.claudedemo.product.domain.Product;
import com.example.claudedemo.product.domain.ProductNotFoundException;
import com.example.claudedemo.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsBySellerId(request.getSellerId())) {
            throw new DuplicateSellerIdException(request.getSellerId());
        }
        Product product = Product.create(request.getName(), request.getSellerId(), request.getCategoryNumber());
        Product saved = productRepository.save(product);
        return ProductResponse.from(saved);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        if (productRepository.existsBySellerIdAndIdNot(request.getSellerId(), id)) {
            throw new DuplicateSellerIdException(request.getSellerId());
        }
        product.update(request.getName(), request.getSellerId(), request.getCategoryNumber());
        Product saved = productRepository.save(product);
        return ProductResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
