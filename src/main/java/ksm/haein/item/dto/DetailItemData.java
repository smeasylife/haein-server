package ksm.haein.item.dto;

import ksm.haein.item.entity.Item;
import ksm.haein.qna.dto.QuestionDto;
import ksm.haein.review.dto.ReviewDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DetailItemData {
    private Long itemId;
    private String name;
    private Integer shippingPrice;
    private String information;
    private String shippingInfo;
    private List<ItemPictureDto> itemPictures;
    private List<ReviewDto> reviews;
    private List<QuestionDto> questions;
    private List<ItemOptionDto> itemOptions;

    // For testing
    public DetailItemData(Long itemId, String name, Integer shippingPrice, String information,
                         String shippingInfo,
                         List<ItemPictureDto> itemPictures, List<ReviewDto> reviews,
                         List<QuestionDto> questions, List<ItemOptionDto> itemOptions) {
        this.itemId = itemId;
        this.name = name;
        this.shippingPrice = shippingPrice;
        this.information = information;
        this.shippingInfo = shippingInfo;
        this.itemPictures = itemPictures;
        this.reviews = reviews;
        this.questions = questions;
        this.itemOptions = itemOptions;
    }

    public
    DetailItemData(Item item) {
        this.itemId = item.getId();
        this.name = item.getName();
        this.shippingPrice = item.getShippingPrice();
        this.information = item.getInformation();
        this.shippingInfo = item.getShippingInfo();
        this.itemPictures = item.getPictures().stream()
                .map(ItemPictureDto::new)
                .collect(Collectors.toList());
        this.reviews = item.getReviews().stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
        this.questions = item.getQuestions().stream()
                .map(QuestionDto::new)
                .collect(Collectors.toList());
        this.itemOptions = item.getItemOptions().stream()
                .map(ItemOptionDto::new)
                .collect(Collectors.toList());
    }
}
