package com.example.growingshopaudience.service;

import com.example.auth.AccessibleUserTypes;
import com.example.domain.category.Category;
import com.example.domain.category.CategoryStatus;
import com.example.domain.user.UserType;
import com.example.growingshopaudience.dto.CategoryRequest;
import com.example.growingshopaudience.dto.CategoryResponse;
import com.example.repository.category.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse.CategoryRes> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse.CategoryRes::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @AccessibleUserTypes({UserType.ADMIN, UserType.SELLER})
    public CategoryResponse.CategoryRes create(CategoryRequest.CategoryReq req) {
        Category category = categoryRepository.save(req.toEntity());

        return CategoryResponse.CategoryRes.from(category);
    }

    @Transactional
    @AccessibleUserTypes(UserType.ADMIN)
    public void delete(Long id) {
        getOne(id).delete();
    }

    @Transactional
    @AccessibleUserTypes(UserType.ADMIN)
    public void inspect(Long id, CategoryStatus status) {
        Category category = getOne(id);

        category.inspect(status);
    }

    private Category getOne(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + " 에 해당하는 카테고리가 존재하지 않습니다."));
    }
}
