package ksm.haein.coupon.controller;

import ksm.haein.coupon.dto.CouponData;
import ksm.haein.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/coupon")
    public ResponseEntity<String> makeCoupon(@RequestBody CouponData couponData) {
        couponService.saveCoupon(couponData);
        return ResponseEntity.ok("Coupon Saved");
    }
}
