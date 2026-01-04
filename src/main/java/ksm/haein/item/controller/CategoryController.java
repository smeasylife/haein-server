package ksm.haein.item.controller;

import jakarta.validation.Valid;
import ksm.haein.item.dto.CategoryCreateRequest;
import ksm.haein.item.dto.CategoryDto;
import ksm.haein.item.dto.CategoryUpdateRequest;
import ksm.haein.item.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ResponseEntity<Long> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        Long categoryId = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request
    ) {
        categoryService.updateCategory(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
