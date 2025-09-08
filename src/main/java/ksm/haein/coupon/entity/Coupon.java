package ksm.haein.coupon.entity;

import jakarta.persistence.*;
import ksm.haein.coupon.dto.CouponData;
import ksm.haein.coupon.enums.DiscountType;
import ksm.haein.user.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private DiscountType discountType;

    private Integer discountValue;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE)
    private ArrayList<MemberCoupon> coupons = new ArrayList<>();
}
