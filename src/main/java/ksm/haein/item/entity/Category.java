package ksm.haein.item.entity;

import jakarta.persistence.*;
import ksm.haein.item.enums.CategoryName;
import lombok.Getter;

import java.util.ArrayList;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryName name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private ArrayList<ItemCategory> itemCategories = new ArrayList<>();
}
