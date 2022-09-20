package com.example.growingshop.domain.product.repository;

import com.example.growingshop.domain.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
