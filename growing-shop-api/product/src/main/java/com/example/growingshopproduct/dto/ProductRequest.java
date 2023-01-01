package com.example.growingshopproduct.dto;

import com.example.domain.product.Product;
import com.example.domain.product.ProductStatus;
import com.example.growingshopproduct.domain.ProductProxy;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProductReq {
        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private int price;

        private Long company;

        public Product toEntity() {
            // TODO - 추후 의존성 분리를 위한 validator 와 converter 분리 시 company, category 기능 적용
            return new Product(name, price, ProductStatus.UNDER_REVIEW, Collections.emptyList());
        }
    }
}
