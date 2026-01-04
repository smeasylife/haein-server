package ksm.haein.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ItemCreateRequest(
    @NotBlank(message = "상품명은 필수입니다.")
    String name,

    @NotNull(message = "배송비는 필수입니다.")
    @Positive(message = "배송비는 양수여야 합니다.")
    Integer shippingPrice,

    @NotBlank(message = "상품 정보는 필수입니다.")
    String information,

    String shippingInfo,

    @NotEmpty(message = "적어도 하나의 이미지 URL이 필요합니다.")
    List<@NotBlank(message = "이미지 URL은 비워있을 수 없습니다.") String> pictureUrls,

    @NotEmpty(message = "적어도 하나의 카테고리가 필요합니다.")
    List<String> categories,

    @NotEmpty(message = "적어도 하나의 옵션이 필요합니다.")
    List<ItemOptionRequest> itemOptions
) {
}
