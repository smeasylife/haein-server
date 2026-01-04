package ksm.haein.item.dto;

import jakarta.validation.constraints.Positive;

import java.util.List;

public record ItemUpdateRequest(
    String name,

    @Positive(message = "배송비는 양수여야 합니다.")
    Integer shippingPrice,

    String information,

    String shippingInfo,

    List<String> pictureUrls,

    List<String> categories,

    List<ItemOptionRequest> itemOptions
) {
}
