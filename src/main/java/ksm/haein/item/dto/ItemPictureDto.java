package ksm.haein.item.dto;

import ksm.haein.item.entity.ItemPicture;

public record ItemPictureDto(String url) {
    public ItemPictureDto(ItemPicture itemPicture) {
        this(itemPicture.getUrl());
    }
}
