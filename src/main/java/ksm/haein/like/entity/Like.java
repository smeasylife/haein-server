package ksm.haein.like.entity;

import jakarta.persistence.*;
import ksm.haein.item.entity.Item;
import ksm.haein.user.entity.Member;
import lombok.Getter;

@Entity
@Getter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
