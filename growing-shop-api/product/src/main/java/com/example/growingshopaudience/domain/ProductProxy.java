package com.example.growingshopaudience.domain;

import com.example.domain.product.Product;
import com.example.domain.product.ProductStatus;
import com.example.growingshopaudience.dto.ProductRequest;

public class ProductProxy {

    private Product product;

    public ProductProxy(Product product) {
        this.product = product;
    }

    public void update(ProductRequest.ProductReq req) {
        product.setName(req.getName());
        product.setPrice(req.getPrice());
    }

    public void delete() {
        product.setStatus(ProductStatus.DELETED);
    }

    public Product getProduct() {
        return this.product;
    }
}
