package com.example.growingshop.domain.product.service;

import com.example.growingshop.domain.auth.accessible.AccessibleUserTypes;
import com.example.growingshop.domain.product.domain.Product;
import com.example.growingshop.domain.product.dto.ProductRequest;
import com.example.growingshop.domain.product.dto.ProductResponse;
import com.example.growingshop.domain.product.repository.ProductRepository;
import com.example.growingshop.domain.user.domain.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    public ProductResponse.ProductRes getByid(Long id) {
        Product product = getById(id);

        return ProductResponse.ProductRes.from(product);
    }

    @Transactional
    @AccessibleUserTypes(UserType.SELLER)
    public ProductResponse.ProductRes create(ProductRequest.ProductReq req) {
        return ProductResponse.ProductRes.from(
                productRepository.save(req.toEntity())
        );
    }

    @Transactional
    @AccessibleUserTypes({UserType.ADMIN, UserType.SELLER})
    public ProductResponse.ProductRes update(Long id, ProductRequest.ProductReq req) {
        Product product = getById(id);
        product.update(req);

        return ProductResponse.ProductRes
                .from(product);
    }

    @Transactional
    @AccessibleUserTypes({UserType.ADMIN, UserType.SELLER})
    public void delete(Long id) {
        Product product = getById(id);

        product.delete();
    }

    private Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + " 에 해당하는 상품이 존재하지 않습니다."));
    }
}
