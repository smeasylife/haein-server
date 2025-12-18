package ksm.haein.cart.controller;

import ksm.haein.cart.service.CartService;
import ksm.haein.config.security.login.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/{itemId}/cart")
    public ResponseEntity<Void> addCart(@PathVariable long itemId, @AuthenticationPrincipal CustomUser customUser) {
        cartService.addCart(customUser.getId(), itemId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
