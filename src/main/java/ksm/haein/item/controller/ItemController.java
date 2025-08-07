package ksm.haein.item.controller;

import ksm.haein.item.dto.ItemData;
import ksm.haein.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items")
    public List<ItemData> getHomeItems(@RequestParam int page) {
        return itemService.getItemData(page);
    }


}
