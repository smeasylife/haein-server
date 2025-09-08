package ksm.haein.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ksm.haein.cart.service.CartService;
import ksm.haein.config.security.login.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "장바구니", description = "장바구니 관련 API")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @Operation(summary = "장바구니에 상품 추가", description = "장바구니에 상품을 추가합니다.")
    @PostMapping("/{itemId}/cart")
    public ResponseEntity<Void> addCart(@PathVariable long itemId, @AuthenticationPrincipal CustomUser customUser) {
        cartService.addCart(customUser.getId(), itemId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
