package ksm.haein.item.dto;

public record ItemData(Long id, String name, Integer basePrice,
                       Integer salePrice, String color, String pictureUrl, Boolean like) {
}
