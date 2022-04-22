package com.example.growingshop.order.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
public class OrderId implements Serializable {
    private String id;
}
