package ksm.haein.item.dto;

public record ItemData(Long id, String name, Integer price,
                       Integer salePrice, String color, String pictureUrl, Boolean like) {
}
