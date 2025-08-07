package ksm.haein.like.controller;

import ksm.haein.config.security.login.CustomUser;
import ksm.haein.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{itemId}/like")
    public ResponseEntity<Void> addLike(@PathVariable Long itemId, @AuthenticationPrincipal CustomUser customUser) {
        likeService.addLike(itemId, customUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
