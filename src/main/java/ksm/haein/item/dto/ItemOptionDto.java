package ksm.haein.item.dto;

import ksm.haein.item.entity.ItemOption;

public record ItemOptionDto(
    Long id,
    String size,
    String color,
    Integer price,
    Integer stock
) {
    public ItemOptionDto(ItemOption itemOption) {
        this(
            itemOption.getId(),
            itemOption.getSize(),
            itemOption.getColor(),
            itemOption.getPrice(),
            itemOption.getStock()
        );
    }
}
