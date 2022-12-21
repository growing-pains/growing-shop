package com.example.growingshopproduct.domain;

import com.example.growingshopproduct.dto.ProductRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Pattern(regexp = "^[a-zA-Z가-힣!@#$%^&*\"'-,.<>?:\\[\\]()]+$")
    private String name;

    @Column(nullable = false)
    @Min(1)
    private Integer price;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "COMPANY", nullable = false)
//    private Company company;

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
