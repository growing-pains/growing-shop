package com.example.growingshoporder.domain;

import com.example.growingshoporder.dto.OrderRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @ManyToOne(targetEntity = Product.class)
//    @JoinColumn(name="product", nullable = false)
    private Long productId;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer price;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;

    private Boolean isDeleted;

    public OrderLine(Long productId, Integer price, Integer quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.isDeleted = false;
    }

    public Long amounts() {
        return price * quantity.longValue();
    }

    public void update(OrderRequest.OrderLineReq req) {
        this.productId = req.getProductId();
        this.price = req.getPrice();
        this.quantity = req.getQuantity();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
