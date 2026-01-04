package ksm.haein.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ksm.haein.item.enums.CategoryName;

import java.util.List;

public record ItemCreateRequest(
    @NotBlank(message = "상품명은 필수입니다.")
    String name,

    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 양수여야 합니다.")
    Integer price,

    @NotNull(message = "할인가는 필수입니다.")
    @Positive(message = "할인가는 양수여야 합니다.")
    Integer salePrice,

    @NotNull(message = "배송비는 필수입니다.")
    @Positive(message = "배송비는 양수여야 합니다.")
    Integer shippingPrice,

    @NotBlank(message = "사이즈는 필수입니다.")
    String size,

    @NotBlank(message = "색상은 필수입니다.")
    String color,

    @NotBlank(message = "상품 정보는 필수입니다.")
    String information,

    @NotEmpty(message = "적어도 하나의 이미지 URL이 필요합니다.")
    List<@NotBlank(message = "이미지 URL은 비어있을 수 없습니다.") String> pictureUrls,

    @NotEmpty(message = "적어도 하나의 카테고리가 필요합니다.")
    List<CategoryName> categories
) {
}
