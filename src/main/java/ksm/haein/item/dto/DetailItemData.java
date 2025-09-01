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
    private Integer price;
    private Integer salePrice;
    private Integer shippingPrice;
    private String size;
    private String color;
    private String information;
    private List<ItemPictureDto> itemPictures;
    private List<ReviewDto> reviews;
    private List<QuestionDto> questions;

    public DetailItemData(Item item) {
        this.itemId = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.salePrice = item.getSalePrice();
        this.shippingPrice = item.getShippingPrice();
        this.size = item.getSize();
        this.color = item.getColor();
        this.information = item.getInformation();
        this.itemPictures = item.getPictures().stream()
                .map(ItemPictureDto::new)
                .collect(Collectors.toList());
        this.reviews = item.getReviews().stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
        this.questions = item.getQuestions().stream()
                .map(QuestionDto::new)
                .collect(Collectors.toList());
    }
}
