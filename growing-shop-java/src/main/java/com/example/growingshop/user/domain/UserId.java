package com.example.growingshop.user.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
public class UserId implements Serializable {
    private Long id;
}
