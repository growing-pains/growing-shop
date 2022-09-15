package com.example.growingshop.domain.product.service;

import com.example.growingshop.domain.auth.accessible.AccessibleUserTypes;
import com.example.growingshop.domain.user.domain.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    @AccessibleUserTypes({UserType.ADMIN, UserType.SELLER})
    public boolean test() {
        return true;
    }
}
