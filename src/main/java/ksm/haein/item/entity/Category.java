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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category parent;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private ArrayList<ItemCategory> itemCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private ArrayList<Category> subCategories = new ArrayList<>();

    public void update(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }
}
