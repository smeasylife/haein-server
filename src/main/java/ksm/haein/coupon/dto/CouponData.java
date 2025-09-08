package ksm.haein.coupon.dto;

import java.time.LocalDateTime;

public record CouponData(String name, String type,
                         Integer value, LocalDateTime startTime, LocalDateTime endTime) {
}
