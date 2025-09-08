package ksm.haein.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ksm.haein.config.security.login.CustomUser;
import ksm.haein.item.dto.DetailItemData;
import ksm.haein.item.dto.ItemData;
import ksm.haein.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "상품", description = "상품 관련 API")
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @Operation(summary = "홈 화면 상품 목록 조회", description = "홈 화면에 보여줄 상품 목록을 조회합니다.")
    @GetMapping("/items")
    public List<ItemData> getHomeItems(@RequestParam int page, @AuthenticationPrincipal CustomUser customUser) {
        if (customUser == null) {
            return itemService.getItemDataWithoutLike(page);
        }

        return itemService.getItemDataWithLike(page, customUser.getId());
    }

    @Operation(summary = "상품 상세 정보 조회", description = "상품의 상세 정보를 조회합니다.")
    @GetMapping("/items/{itemId}")
    public DetailItemData getDetailItemData(@RequestParam long itemId) {
        return itemService.getDetailItemData(itemId);
    }


}
