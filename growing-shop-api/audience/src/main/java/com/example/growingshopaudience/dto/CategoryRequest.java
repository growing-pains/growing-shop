package com.example.growingshopaudience.dto;

import com.example.domain.category.Category;
import com.example.domain.category.CategoryStatus;
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
            return new Category(name, CategoryStatus.UNDER_REVIEW);
        }
    }
}
