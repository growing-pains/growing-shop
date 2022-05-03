package com.example.growingshop.domain.company.domain;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class CompanyId implements Serializable {
    @Column(name = "id")
    private Long value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyId companyId = (CompanyId) o;
        return Objects.equals(value, companyId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
