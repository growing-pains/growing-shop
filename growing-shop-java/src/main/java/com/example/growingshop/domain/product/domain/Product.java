package com.example.growingshop.domain.product.domain;

import com.example.growingshop.domain.company.domain.Company;
import com.example.growingshop.domain.product.dto.ProductRequest;
import com.example.growingshop.global.validator.StringChecker;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 100)
    @StringChecker(includeLowerEn = true, includeUpperEn = true, includeKo = true, includeNumber = true, includeSpecialCharacter = "!@#$%^&*\"'-,.<>?:[]()")
    private String name;

    @Column(nullable = false)
    @Min(1)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ProductCategory> categories = new ArrayList<>();

    public void update(ProductRequest.ProductReq req) {
        this.name = req.getName();
        this.price = req.getPrice();
    }

    public void delete() {
        this.status = ProductStatus.DELETED;
    }
}
