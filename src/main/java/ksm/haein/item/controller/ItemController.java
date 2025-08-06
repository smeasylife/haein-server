package ksm.haein.item.controller;

import ksm.haein.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
}
