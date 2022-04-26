package com.example.growingshop.order.domain;

import com.example.growingshop.product.domain.ProductId;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Embeddable
public class OrderLine {
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "product", nullable = false)))
    private ProductId product;

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
