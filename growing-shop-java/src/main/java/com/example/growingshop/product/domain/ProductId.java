package com.example.growingshop.product.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class ProductId implements Serializable {
    private Long id;
}