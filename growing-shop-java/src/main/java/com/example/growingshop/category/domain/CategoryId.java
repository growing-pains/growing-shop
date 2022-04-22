package com.example.growingshop.category.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
public class CategoryId implements Serializable {
    private Long id;
}
