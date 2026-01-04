package ksm.haein.item.dto;

import ksm.haein.item.entity.Category;

import java.util.List;

public record CategoryDto(
        Long id,
        String name,
        Long parentId,
        List<CategoryDto> subCategories
) {
    public CategoryDto(Category category) {
        this(
                category.getId(),
                category.getName(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getSubCategories().stream()
                        .map(CategoryDto::new)
                        .toList()
        );
    }
}
