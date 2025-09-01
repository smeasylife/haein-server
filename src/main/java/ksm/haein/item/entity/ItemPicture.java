package ksm.haein.item.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ItemPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
