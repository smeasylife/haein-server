package ksm.haein.coupon.service;

import ksm.haein.coupon.dto.CouponData;
import ksm.haein.coupon.entity.Coupon;
import ksm.haein.coupon.enums.DiscountType;
import ksm.haein.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {
    private final CouponRepository couponRepository;

    public void saveCoupon(CouponData couponData) {
        if (couponData.type().equals("PERCENT")) {
            couponRepository.save(getPercentCoupon(couponData));
        } else if (couponData.type().equals("DISCOUNT")) {
            couponRepository.save(getFixedCoupon(couponData));
        }
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
}
