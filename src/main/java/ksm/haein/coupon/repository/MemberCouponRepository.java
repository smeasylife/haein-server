package ksm.haein.coupon.repository;

import ksm.haein.coupon.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    void deleteAllByCouponId(Long couponId);
}
