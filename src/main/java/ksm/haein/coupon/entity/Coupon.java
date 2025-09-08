package ksm.haein.coupon.entity;

import jakarta.persistence.*;
import ksm.haein.user.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Getter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE)
    private ArrayList<MemberCoupon> coupons = new ArrayList<>();
}
