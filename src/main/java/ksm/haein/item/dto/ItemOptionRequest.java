package ksm.haein.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemOptionRequest(
    @NotBlank(message = "사이즈는 필수입니다.")
    String size,

    @NotBlank(message = "색상은 필수입니다.")
    String color,

    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 양수여야 합니다.")
    Integer price,

    @NotNull(message = "재고는 필수입니다.")
    @Positive(message = "재고는 0 이상이어야 합니다.")
    Integer stock
) {
}
