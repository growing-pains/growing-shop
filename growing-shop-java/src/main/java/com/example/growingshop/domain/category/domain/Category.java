package com.example.growingshop.domain.category.domain;

import com.example.growingshop.domain.product.domain.ProductId;
import com.example.growingshop.global.validator.StringAnyContain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {
    @EmbeddedId
    private ProductId id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 100)
    @StringAnyContain(checkContainLowerEn = true, checkContainUpperEn = true, checkContainKo = true, checkContainNumber = true, hasContainSpecialCharacter = "-.\"'")
    private String name;
}
