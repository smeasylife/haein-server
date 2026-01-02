package ksm.haein.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksm.haein.coupon.dto.CouponData;
import ksm.haein.coupon.enums.DiscountType;
import ksm.haein.coupon.service.CouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = CouponController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
    }
)
@DisplayName("쿠폰 컨트롤러 테스트")
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 생성 - 성공 (PERCENT 할인)")
    void testMakeCouponSuccessPercent() throws Exception {
        // Given
        CouponData couponData = new CouponData(
                " summer Sale",
                DiscountType.PERCENT,
                10,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7)
        );
        doNothing().when(couponService).saveCoupon(any(CouponData.class));

        // When & Then
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Coupon Saved"));

        verify(couponService).saveCoupon(any(CouponData.class));
    }

    @Test
    @DisplayName("쿠폰 생성 - 성공 (FIXED_AMOUNT 할인)")
    void testMakeCouponSuccessFixedAmount() throws Exception {
        // Given
        CouponData couponData = new CouponData(
                "1000원 할인",
                DiscountType.FIXED_AMOUNT,
                1000,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7)
        );
        doNothing().when(couponService).saveCoupon(any(CouponData.class));

        // When & Then
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Coupon Saved"));

        verify(couponService).saveCoupon(any(CouponData.class));
    }

    @Test
    @DisplayName("쿠폰 생성 - 실패 (이름 누락)")
    void testMakeCouponMissingName() throws Exception {
        // Given
        CouponData couponData = new CouponData(
                "",
                DiscountType.PERCENT,
                10,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7)
        );

        // When & Then
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("쿠폰 생성 - 실패 (타입 누락)")
    void testMakeCouponMissingType() throws Exception {
        // Given
        String invalidJson = """
                {
                    "name": "Summer Sale",
                    "value": 10,
                    "startTime": "2023-12-19T10:00:00",
                    "endTime": "2023-12-25T23:59:59"
                }
                """;

        // When & Then
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("쿠폰 생성 - 실패 (종료 시간이 시작 시간보다 빠름)")
    void testMakeCouponInvalidTimeRange() throws Exception {
        // Given
        CouponData couponData = new CouponData(
                "Summer Sale",
                DiscountType.PERCENT,
                10,
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now()  // 종료가 시작보다 빠름
        );

        // When & Then
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("쿠폰 생성 - 실패 (음수 할인율)")
    void testMakeCouponNegativeValue() throws Exception {
        // Given
        CouponData couponData = new CouponData(
                "Summer Sale",
                DiscountType.PERCENT,
                -10,  // 음수
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7)
        );

        // When & Then
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("쿠폰 생성 - 실패 (PERCENT 타입인데 100 초과)")
    void testMakeCouponPercentOver100() throws Exception {
        // Given
        CouponData couponData = new CouponData(
                "Summer Sale",
                DiscountType.PERCENT,
                150,  // 100% 초과
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7)
        );

        // When & Then
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
