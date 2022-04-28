package com.example.growingshop.product.domain;

import com.example.growingshop.category.domain.CategoryId;
import com.example.growingshop.company.domain.CompanyId;
import com.example.growingshop.validator.StringContain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {
    @EmbeddedId
    private ProductId id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 100)
    @StringContain(checkLowerEn = true, checkUpperEn = true, checkKo = true, checkNumber = true, hasSpecialCharacter = "!@#$%^&*\"'-,.<>?:[]()")
    private String name;

    @Column(nullable = false)
    @Min(1)
    private Integer price;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "company", nullable = false)))
    private CompanyId company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @ElementCollection
    @CollectionTable(name = "product_category", joinColumns = {@JoinColumn(name = "product")})
    private List<CategoryId> categories = new ArrayList<>();
}
