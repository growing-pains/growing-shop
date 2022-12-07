package com.example.growingshopproduct.repository;

import com.example.growingshopproduct.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
