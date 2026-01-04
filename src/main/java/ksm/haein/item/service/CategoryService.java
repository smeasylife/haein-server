package ksm.haein.item.service;

import jakarta.persistence.EntityNotFoundException;
import ksm.haein.item.dto.CategoryCreateRequest;
import ksm.haein.item.dto.CategoryDto;
import ksm.haein.item.dto.CategoryUpdateRequest;
import ksm.haein.item.entity.Category;
import ksm.haein.item.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        List<Category> rootCategories = categoryRepository.findAllByParentIsNull();
        return rootCategories.stream()
                .map(CategoryDto::new)
                .toList();
    }

    @Transactional
    public Long createCategory(CategoryCreateRequest request) {
        Category parent = null;
        if (request.parentId() != null) {
            parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found: " + request.parentId()));
        }

        Category category = Category.builder()
                .name(request.name())
                .parent(parent)
                .build();

        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public void updateCategory(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));

        Category parent = null;
        if (request.parentId() != null) {
            parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found: " + request.parentId()));
        }

        category.update(request.name(), parent);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
