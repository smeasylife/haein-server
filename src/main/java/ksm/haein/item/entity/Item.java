package ksm.haein.item.entity;

import jakarta.persistence.*;
import ksm.haein.like.entity.Like;
import ksm.haein.qna.entity.Question;
import ksm.haein.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private Integer salePrice;

    private Integer shippingPrice;

    private String size;

    private String color;

    private String information;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private ArrayList<ItemPicture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private ArrayList<ItemCategory> itemCategories = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private ArrayList<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private ArrayList<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private ArrayList<Question> questions = new ArrayList<>();

    public void update(String name, Integer price, Integer salePrice, Integer shippingPrice,
                      String size, String color, String information) {
        if (name != null) {
            this.name = name;
        }
        if (price != null) {
            this.price = price;
        }
        if (salePrice != null) {
            this.salePrice = salePrice;
        }
        if (shippingPrice != null) {
            this.shippingPrice = shippingPrice;
        }
        if (size != null) {
            this.size = size;
        }
        if (color != null) {
            this.color = color;
        }
        if (information != null) {
            this.information = information;
        }
    }
}
