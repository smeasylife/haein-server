package ksm.haein.user.entity;

import jakarta.persistence.*;
import ksm.haein.coupon.entity.MemberCoupon;
import ksm.haein.like.entity.Like;
import ksm.haein.review.entity.Review;
import ksm.haein.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private Role role;

    private LocalDateTime createdAt;

    private Integer point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private ArrayList<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private ArrayList<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private ArrayList<MemberCoupon> coupons = new ArrayList<>();

}
