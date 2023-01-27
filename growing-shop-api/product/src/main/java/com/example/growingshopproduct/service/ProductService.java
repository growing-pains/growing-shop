package com.example.growingshopproduct.service;

import com.example.auth.AccessibleUserTypes;
import com.example.domain.user.UserType;
import com.example.growingshopproduct.domain.ProductProxy;
import com.example.growingshopproduct.dto.ProductRequest;
import com.example.growingshopproduct.dto.ProductResponse;
import com.example.repository.product.ProductRepository;
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
        ProductProxy proxy = getProxy(id);

        return ProductResponse.ProductRes
                .from(proxy.getProduct());
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
        ProductProxy proxy = getProxy(id);
        proxy.update(req);

        return ProductResponse.ProductRes
                .from(proxy.getProduct());
    }

    @Transactional
    @AccessibleUserTypes({UserType.ADMIN, UserType.SELLER})
    public void delete(Long id) {
        ProductProxy proxy = getProxy(id);

        proxy.delete();
    }

    private ProductProxy getProxy(Long id) {
        return new ProductProxy(
                productRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(id + " 에 해당하는 상품이 존재하지 않습니다."))
        );
    }
}
