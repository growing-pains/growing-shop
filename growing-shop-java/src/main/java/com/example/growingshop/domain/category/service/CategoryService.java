package com.example.growingshop.domain.category.service;

import com.example.growingshop.domain.auth.accessible.AccessibleUserTypes;
import com.example.growingshop.domain.category.domain.Category;
import com.example.growingshop.domain.category.domain.CategoryStatus;
import com.example.growingshop.domain.category.dto.CategoryRequest;
import com.example.growingshop.domain.category.dto.CategoryResponse;
import com.example.growingshop.domain.category.repository.CategoryRepository;
import com.example.growingshop.domain.user.domain.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

        if (status == CategoryStatus.NORMAL) {
            category.confirm();
        }
        if (status == CategoryStatus.REJECTED) {
            category.reject();
        }
    }

    private Category getOne(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + " 에 해당하는 카테고리가 존재하지 않습니다."));
    }
}
