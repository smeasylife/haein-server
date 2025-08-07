package ksm.haein.item.entity;

import jakarta.persistence.*;
import ksm.haein.like.entity.Like;
import ksm.haein.review.entity.Review;
import lombok.Getter;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Getter
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
}
