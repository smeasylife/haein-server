package ksm.haein.item.dto;

import jakarta.validation.constraints.Positive;
import ksm.haein.item.enums.CategoryName;

import java.util.List;

public record ItemUpdateRequest(
    String name,

    @Positive(message = "가격은 양수여야 합니다.")
    Integer price,

    @Positive(message = "할인가는 양수여야 합니다.")
    Integer salePrice,

    @Positive(message = "배송비는 양수여야 합니다.")
    Integer shippingPrice,

    String size,

    String color,

    String information,

    List<String> pictureUrls,

    List<CategoryName> categories
) {
}
