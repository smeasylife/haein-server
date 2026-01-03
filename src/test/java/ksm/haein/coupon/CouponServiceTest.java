package ksm.haein.coupon;

import ksm.haein.coupon.dto.CouponData;
import ksm.haein.coupon.entity.Coupon;
import ksm.haein.coupon.enums.DiscountType;
import ksm.haein.coupon.repository.CouponRepository;
import ksm.haein.coupon.service.CouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("쿠폰 서비스 테스트")
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 생성 - PERCENT 할인")
    void testSaveCouponPercent() {
        // Given
        CouponData couponData = new CouponData(
                "Summer Sale",
                DiscountType.PERCENT,
                10,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7)
        );

        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        couponService.saveCoupon(couponData);

        // Then
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    @DisplayName("쿠폰 생성 - FIXED_AMOUNT 할인")
    void testSaveCouponFixedAmount() {
        // Given
        CouponData couponData = new CouponData(
                "1000원 할인",
                DiscountType.FIXED_AMOUNT,
                1000,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7)
        );

        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        couponService.saveCoupon(couponData);

        // Then
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    @DisplayName("쿠폰 생성 - 데이터 검증")
    void testSaveCouponDataIntegrity() {
        // Given
        String name = "Test Coupon";
        DiscountType type = DiscountType.PERCENT;
        Integer value = 20;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(7);

        CouponData couponData = new CouponData(name, type, value, startTime, endTime);

        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> {
            Coupon coupon = invocation.getArgument(0);
            // 데이터가 올바르게 전달되었는지 확인
            assertEquals(name, coupon.getName());
            assertEquals(type, coupon.getDiscountType());
            assertEquals(value, coupon.getDiscountValue());
            assertEquals(startTime, coupon.getStartTime());
            assertEquals(endTime, coupon.getEndTime());
            return coupon;
        });

        // When
        couponService.saveCoupon(couponData);

        // Then
        verify(couponRepository).save(any(Coupon.class));
    }
}
