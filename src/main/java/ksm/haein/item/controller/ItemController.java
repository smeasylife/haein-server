package ksm.haein.item.controller;

import ksm.haein.config.security.login.CustomUser;
import ksm.haein.item.dto.ItemData;
import ksm.haein.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items")
    public List<ItemData> getHomeItems(@RequestParam int page, @AuthenticationPrincipal CustomUser customUser) {
        if (customUser == null) {
            return itemService.getItemDataWithoutLike(page);
        }

        return itemService.getItemDataWithLike(page, customUser.getId());
    }


}
