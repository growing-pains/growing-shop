package com.example.growingshopaudience.category.service;

import com.example.growingshopaudience.category.domain.Category;
import com.example.growingshopaudience.category.domain.CategoryStatus;
import com.example.growingshopaudience.category.dto.CategoryRequest;
import com.example.growingshopaudience.category.dto.CategoryResponse;
import com.example.growingshopaudience.category.repository.CategoryRepository;
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
//    @AccessibleUserTypes({UserType.ADMIN, UserType.SELLER})
    // TODO - 기존 AccessibleUserTypes 을 auth 모듈에서 처리할 수 있는 방법 찾기
    public CategoryResponse.CategoryRes create(CategoryRequest.CategoryReq req) {
        Category category = categoryRepository.save(req.toEntity());

        return CategoryResponse.CategoryRes.from(category);
    }

    @Transactional
//    @AccessibleUserTypes(UserType.ADMIN)
    public void delete(Long id) {
        getOne(id).delete();
    }

    @Transactional
//    @AccessibleUserTypes(UserType.ADMIN)
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
