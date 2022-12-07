package com.example.growingshopaudience.category.api;

import com.example.growingshopaudience.category.domain.CategoryStatus;
import com.example.growingshopaudience.category.dto.CategoryRequest;
import com.example.growingshopaudience.category.dto.CategoryResponse;
import com.example.growingshopaudience.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse.CategoryRes>> findAll() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse.CategoryRes> create(@RequestBody CategoryRequest.CategoryReq req) {
        return new ResponseEntity<>(categoryService.create(req), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        categoryService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/inspect")
    public ResponseEntity inspect(@PathVariable Long id, @RequestParam CategoryStatus status) {
        categoryService.inspect(id, status);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
