package com.example.growingshop.domain.product.api;

import com.example.growingshop.domain.product.dto.ProductRequest;
import com.example.growingshop.domain.product.dto.ProductResponse;
import com.example.growingshop.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse.ProductRes>> findAll() {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse.ProductRes> findById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getByid(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponse.ProductRes> create(@RequestBody ProductRequest.ProductReq req) {
        return new ResponseEntity<>(productService.create(req), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse.ProductRes> update(@PathVariable Long id, @RequestBody ProductRequest.ProductReq req) {
        return new ResponseEntity<>(productService.update(id, req), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        productService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
