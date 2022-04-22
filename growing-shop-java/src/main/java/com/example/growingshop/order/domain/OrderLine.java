package com.example.growingshop.order.domain;

import com.example.growingshop.product.domain.ProductId;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Embeddable
public class OrderLine {
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "id", column = @Column(name = "product")))
    private ProductId product;

    private Integer price;

    private Integer quantity;

    public Long amounts() {
        return price * quantity.longValue();
    }
}
