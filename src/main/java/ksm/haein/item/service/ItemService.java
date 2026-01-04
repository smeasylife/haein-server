package ksm.haein.item.service;

import jakarta.persistence.EntityNotFoundException;
import ksm.haein.item.dto.DetailItemData;
import ksm.haein.item.dto.ItemCreateRequest;
import ksm.haein.item.dto.ItemData;
import ksm.haein.item.dto.ItemOptionRequest;
import ksm.haein.item.dto.ItemUpdateRequest;
import ksm.haein.item.entity.Category;
import ksm.haein.item.entity.Item;
import ksm.haein.item.entity.ItemCategory;
import ksm.haein.item.entity.ItemOption;
import ksm.haein.item.entity.ItemPicture;
import ksm.haein.item.repository.CategoryRepository;
import ksm.haein.item.repository.ItemCategoryRepository;
import ksm.haein.item.repository.ItemOptionRepository;
import ksm.haein.item.repository.ItemPictureRepository;
import ksm.haein.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemPictureRepository itemPictureRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemOptionRepository itemOptionRepository;

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

    @Transactional
    public Long createItem(ItemCreateRequest request) {
        Item item = Item.builder()
                .name(request.name())
                .shippingPrice(request.shippingPrice())
                .information(request.information())
                .shippingInfo(request.shippingInfo())
                .createdAt(LocalDateTime.now())
                .build();

        itemRepository.save(item);

        for (String url : request.pictureUrls()) {
            ItemPicture picture = ItemPicture.builder()
                    .url(url)
                    .item(item)
                    .build();
            itemPictureRepository.save(picture);
        }

        for (String categoryName : request.categories()) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));

            ItemCategory itemCategory = ItemCategory.builder()
                    .item(item)
                    .category(category)
                    .build();
            itemCategoryRepository.save(itemCategory);
        }

        for (ItemOptionRequest optionRequest : request.itemOptions()) {
            ItemOption itemOption = ItemOption.builder()
                    .size(optionRequest.size())
                    .color(optionRequest.color())
                    .price(optionRequest.price())
                    .stock(optionRequest.stock())
                    .item(item)
                    .build();
            itemOptionRepository.save(itemOption);
        }

        return item.getId();
    }

    @Transactional
    public void updateItem(Long itemId, ItemUpdateRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + itemId));

        item.update(
                request.name(),
                request.shippingPrice(),
                request.information(),
                request.shippingInfo()
        );

        if (request.pictureUrls() != null) {
            updateItemPictures(item, request.pictureUrls());
        }

        if (request.categories() != null) {
            updateItemCategories(item, request.categories());
        }

        if (request.itemOptions() != null) {
            updateItemOptions(item, request.itemOptions());
        }

        itemRepository.save(item);
    }

    private void updateItemCategories(Item item, List<String> requestedCategories) {
        List<ItemCategory> existingCategories = itemCategoryRepository.findAllByItem(item);

        Set<String> existingNames = existingCategories.stream()
                .map(ic -> ic.getCategory().getName())
                .collect(Collectors.toSet());

        Set<String> toAdd = new HashSet<>(requestedCategories);
        toAdd.removeAll(existingNames);

        Set<String> toRemove = new HashSet<>(existingNames);
        toRemove.removeAll(requestedCategories);

        for (String categoryName : toRemove) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));
            itemCategoryRepository.deleteByItemAndCategory(item, category);
        }

        for (String categoryName : toAdd) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));
            ItemCategory itemCategory = ItemCategory.builder()
                    .item(item)
                    .category(category)
                    .build();
            itemCategoryRepository.save(itemCategory);
        }
    }

    private void updateItemPictures(Item item, List<String> requestedUrls) {
        List<ItemPicture> existingPictures = itemPictureRepository.findAllByItem(item);

        Set<String> existingUrls = existingPictures.stream()
                .map(ItemPicture::getUrl)
                .collect(Collectors.toSet());

        Set<String> toAdd = new HashSet<>(requestedUrls);
        toAdd.removeAll(existingUrls);

        Set<String> toRemove = new HashSet<>(existingUrls);
        toRemove.removeAll(requestedUrls);

        for (String url : toRemove) {
            itemPictureRepository.deleteByItemAndUrl(item, url);
        }

        for (String url : toAdd) {
            ItemPicture picture = ItemPicture.builder()
                    .url(url)
                    .item(item)
                    .build();
            itemPictureRepository.save(picture);
        }
    }

    private void updateItemOptions(Item item, List<ItemOptionRequest> requestedOptions) {
        List<ItemOption> existingOptions = itemOptionRepository.findAllByItemId(item.getId());

        // 기존 옵션 모두 삭제 후 다시 생성 (단순화 접근)
        itemOptionRepository.deleteAllByItemId(item.getId());

        for (ItemOptionRequest optionRequest : requestedOptions) {
            ItemOption itemOption = ItemOption.builder()
                    .size(optionRequest.size())
                    .color(optionRequest.color())
                    .price(optionRequest.price())
                    .stock(optionRequest.stock())
                    .item(item)
                    .build();
            itemOptionRepository.save(itemOption);
        }
    }
}
