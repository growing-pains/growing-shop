package com.example.growingshop.domain.product.service;

import com.example.growingshop.domain.auth.accessible.AdminUser;
import com.example.growingshop.domain.auth.accessible.SellerUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    @AdminUser
    @SellerUser
    public boolean test() {
        return true;
    }
}
