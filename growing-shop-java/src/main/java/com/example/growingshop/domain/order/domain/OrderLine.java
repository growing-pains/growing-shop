package com.example.growingshop.domain.order.domain;

import com.example.growingshop.domain.order.dto.OrderRequest;
import com.example.growingshop.domain.product.domain.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name="PRODUCT", nullable = false)
    private Long productId;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer price;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;

    public OrderLine(Long productId, Integer price, Integer quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long amounts() {
        return price * quantity.longValue();
    }

    public void update(OrderRequest.OrderLineReq req) {
        this.productId = req.getProductId();
        this.price = req.getPrice();
        this.quantity = req.getQuantity();
    }
}
