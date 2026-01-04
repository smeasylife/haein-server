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

    private Integer basePrice;

    private Integer shippingPrice;

    private String information;

    private String shippingInfo;

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

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private ArrayList<ItemOption> itemOptions = new ArrayList<>();

    public void update(String name, Integer shippingPrice, String information, String shippingInfo) {
        if (name != null) {
            this.name = name;
        }
        if (shippingPrice != null) {
            this.shippingPrice = shippingPrice;
        }
        if (information != null) {
            this.information = information;
        }
        if (shippingInfo != null) {
            this.shippingInfo = shippingInfo;
        }
    }
}
