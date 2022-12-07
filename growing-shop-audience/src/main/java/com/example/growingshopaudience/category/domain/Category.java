package com.example.growingshopaudience.category.domain;

import com.example.growingshopaudience.category.dto.CategoryRequest;
import com.example.growingshopcommon.validator.StringChecker;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 100)
    @StringChecker(includeLowerEn = true, includeUpperEn = true, includeKo = true, includeNumber = true, includeSpecialCharacter = "-.\"'")
    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryStatus status;

    public Category(String name) {
        this.name = name;
        this.status = CategoryStatus.UNDER_REVIEW;
    }

    public void update(CategoryRequest.CategoryReq req) {
        this.name = req.getName();
    }

    public void delete() {
        this.status = CategoryStatus.DELETED;
    }

    public void reject() {
        this.status = CategoryStatus.REJECTED;
    }

    public void confirm() {
        this.status = CategoryStatus.NORMAL;
    }
}
