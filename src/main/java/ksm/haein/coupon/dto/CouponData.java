package ksm.haein.coupon.dto;

import ksm.haein.coupon.enums.DiscountType;

import java.time.LocalDateTime;

public record CouponData(String name, DiscountType type,
                         Integer value, LocalDateTime startTime, LocalDateTime endTime) {
}
