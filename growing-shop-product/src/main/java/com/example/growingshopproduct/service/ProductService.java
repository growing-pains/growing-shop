package com.example.growingshopproduct.service;

import com.example.growingshopproduct.domain.Product;
import com.example.growingshopproduct.dto.ProductRequest;
import com.example.growingshopproduct.dto.ProductResponse;
import com.example.growingshopproduct.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse.ProductRes> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse.ProductRes::from)
                .collect(Collectors.toList());
    }

    public ProductResponse.ProductRes getById(Long id) {
        Product product = getOne(id);

        return ProductResponse.ProductRes.from(product);
    }

    @Transactional
//    @AccessibleUserTypes(UserType.SELLER)
    public ProductResponse.ProductRes create(ProductRequest.ProductReq req) {
        return ProductResponse.ProductRes.from(
                productRepository.save(req.toEntity())
        );
    }

    @Transactional
//    @AccessibleUserTypes({UserType.ADMIN, UserType.SELLER})
    public ProductResponse.ProductRes update(Long id, ProductRequest.ProductReq req) {
        Product product = getOne(id);
        product.update(req);

        return ProductResponse.ProductRes
                .from(product);
    }

    @Transactional
//    @AccessibleUserTypes({UserType.ADMIN, UserType.SELLER})
    public void delete(Long id) {
        Product product = getOne(id);

        product.delete();
    }

    private Product getOne(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + " 에 해당하는 상품이 존재하지 않습니다."));
    }
}
