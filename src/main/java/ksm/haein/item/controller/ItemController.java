package ksm.haein.item.controller;

import jakarta.validation.Valid;
import ksm.haein.config.security.login.CustomUser;
import ksm.haein.item.dto.DetailItemData;
import ksm.haein.item.dto.ItemCreateRequest;
import ksm.haein.item.dto.ItemData;
import ksm.haein.item.dto.ItemUpdateRequest;
import ksm.haein.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    @GetMapping("/items/{itemId}")
    public DetailItemData getDetailItemData(@PathVariable long itemId) {
        return itemService.getDetailItemData(itemId);
    }

    @PostMapping("/items")
    public ResponseEntity<Long> createItem(@Valid @RequestBody ItemCreateRequest request) {
        Long itemId = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Void> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemUpdateRequest request
    ) {
        itemService.updateItem(itemId, request);
        return ResponseEntity.ok().build();
    }
}
