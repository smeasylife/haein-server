package ksm.haein.coupon.service;

import ksm.haein.coupon.dto.CouponData;
import ksm.haein.coupon.entity.Coupon;
import ksm.haein.coupon.entity.MemberCoupon;
import ksm.haein.coupon.enums.DiscountType;
import ksm.haein.coupon.repository.CouponRepository;
import ksm.haein.coupon.repository.MemberCouponRepository;
import ksm.haein.user.entity.Member;
import ksm.haein.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveCoupon(CouponData couponData) {
        Coupon coupon;
        switch (couponData.type()) {
            case PERCENT -> coupon = couponRepository.save(getPercentCoupon(couponData));
            case FIXED_AMOUNT -> coupon = couponRepository.save(getFixedCoupon(couponData));
            default -> throw new IllegalArgumentException("Invalid coupon type");
        }

        // 모든 회원에게 쿠폰 발급
        issueCouponToAllMembers(coupon);
    }

    private void issueCouponToAllMembers(Coupon coupon) {
        memberRepository.findAll().forEach(member -> {
            MemberCoupon memberCoupon = MemberCoupon.builder()
                    .member(member)
                    .coupon(coupon)
                    .createdAt(LocalDateTime.now())
                    .build();
            memberCouponRepository.save(memberCoupon);
        });
    }

    private Coupon getPercentCoupon(CouponData couponData) {
        return Coupon.builder()
                .name(couponData.name())
                .discountType(DiscountType.PERCENT)
                .discountValue(couponData.value())
                .startTime(couponData.startTime())
                .endTime(couponData.endTime())
                .build();
    }

    private Coupon getFixedCoupon(CouponData couponData) {
        return Coupon.builder()
                .name(couponData.name())
                .discountType(DiscountType.FIXED_AMOUNT)
                .discountValue(couponData.value())
                .startTime(couponData.startTime())
                .endTime(couponData.endTime())
                .build();
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found: " + couponId));

        // 모든 멤버의 쿠폰 삭제
        memberCouponRepository.deleteAllByCouponId(couponId);

        // 쿠폰 삭제
        couponRepository.delete(coupon);
    }
}
