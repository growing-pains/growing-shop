package com.example.growingshop.order.domain;

import com.example.growingshop.product.domain.ProductId;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Getter
@Embeddable
public class OrderLine {
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "id", column = @Column(name = "product", nullable = false)))
    private ProductId product;

    @Column(nullable = false)
    @Min(1)
    private Integer price;

    @Column(nullable = false)
    @Min(1)
    private Integer quantity;

    public Long amounts() {
        return price * quantity.longValue();
    }
}
