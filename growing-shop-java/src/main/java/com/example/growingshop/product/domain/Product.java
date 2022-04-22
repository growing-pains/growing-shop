package com.example.growingshop.product.domain;

import com.example.growingshop.category.domain.CategoryId;
import com.example.growingshop.company.domain.CompanyId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Product {
    @EmbeddedId
    private ProductId id;

    @Column(nullable = false)
    private Integer price;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "id", column = @Column(name = "company")))
    private CompanyId company;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ElementCollection
    @CollectionTable(name = "product_category", joinColumns = {@JoinColumn(name = "product")})
    private List<CategoryId> categories = new ArrayList<>();
}
