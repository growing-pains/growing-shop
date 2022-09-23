package com.example.growingshop.domain.category.dto;

import com.example.growingshop.domain.category.domain.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CategoryReq {
        private String name;

        public Category toEntity() {
            return new Category(name);
        }
    }
}
