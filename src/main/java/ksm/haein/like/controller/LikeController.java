package ksm.haein.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "좋아요", description = "좋아요 관련 API")
@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "좋아요 추가", description = "상품에 좋아요를 추가합니다.")
    @PostMapping("/{itemId}/like")
    public ResponseEntity<Void> addLike(@PathVariable Long itemId, @AuthenticationPrincipal CustomUser customUser) {
        likeService.addLike(itemId, customUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
