package com.example.growingshop.company.domain;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Embeddable
public class CompanyId implements Serializable {
    private Long id;
}
