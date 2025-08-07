package ksm.haein.item.service;

import ksm.haein.item.dto.ItemData;
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

    public List<ItemData> getItemData( int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<ItemData> pageData = itemRepository.findItemByPage(pageable);
        return pageData.getContent();
    }
}
