package ksm.haein.item.service;

import jakarta.persistence.EntityNotFoundException;
import ksm.haein.item.dto.DetailItemData;
import ksm.haein.item.dto.ItemData;
import ksm.haein.item.entity.Item;
import ksm.haein.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    public List<ItemData> getItemDataWithoutLike(int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<ItemData> pageData = itemRepository.findItemByPageWithoutLike(pageable);
        return pageData.getContent();
    }

    public List<ItemData> getItemDataWithLike(int page, long memberId) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<ItemData> pageData = itemRepository.findItemByPageWithLike(pageable, memberId);
        return pageData.getContent();
    }

    public DetailItemData getDetailItemData(long itemId) {
        Item item = itemRepository.findItemWithDetailsById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));
        return new DetailItemData(item);
    }
}
