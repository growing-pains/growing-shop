package com.example.growingshop.product.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
public class ProductId implements Serializable {
    private Long id;
}
