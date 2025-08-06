package ksm.haein.item.entity;

import jakarta.persistence.*;
import lombok.Getter;

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
}
