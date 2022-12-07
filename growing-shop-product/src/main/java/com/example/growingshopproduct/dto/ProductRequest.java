package com.example.growingshopproduct.dto;

import com.example.growingshopproduct.domain.Product;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
            return Product.builder()
                    .name(name)
                    .price(price)
                    .build();
        }
    }
}
