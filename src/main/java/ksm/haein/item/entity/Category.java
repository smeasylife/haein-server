package ksm.haein.item.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private ArrayList<ItemCategory> itemCategories = new ArrayList<>();
}
