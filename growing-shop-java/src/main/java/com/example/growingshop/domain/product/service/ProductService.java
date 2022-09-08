package com.example.growingshop.domain.product.service;

import com.example.growingshop.domain.auth.accessible.AdminUser;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @AdminUser
    public boolean test() {
        return true;
    }
}
