package com.example.growingshop.domain.order.domain;

import com.example.growingshop.domain.product.domain.Product;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Entity
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`order`", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product", nullable = false)
    private Product product;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer price;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;

    public Long amounts() {
        return price * quantity.longValue();
    }
}
