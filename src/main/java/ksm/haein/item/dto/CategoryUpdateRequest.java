package ksm.haein.item.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateRequest(
        @NotBlank(message = "카테고리명은 필수입니다.")
        String name,
        Long parentId
) {
}
