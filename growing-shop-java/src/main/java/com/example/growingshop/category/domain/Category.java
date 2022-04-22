package com.example.growingshop.category.domain;

import com.example.growingshop.product.domain.ProductId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @EmbeddedId
    private ProductId id;

    @Column(nullable = false)
    @NonNull
    private String name;
}
