package com.example.growingshopaudience.dto;

import com.example.domain.category.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CategoryRes {
        private Long id;
        private String name;

        public static CategoryRes from(Category category) {
            CategoryRes result = new CategoryRes();

            result.id = category.getId();
            result.name = category.getName();

            return result;
        }
    }
}