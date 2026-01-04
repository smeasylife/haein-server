package ksm.haein.item.service;

import jakarta.persistence.EntityNotFoundException;
import ksm.haein.item.dto.DetailItemData;
import ksm.haein.item.dto.ItemCreateRequest;
import ksm.haein.item.dto.ItemData;
import ksm.haein.item.dto.ItemUpdateRequest;
import ksm.haein.item.entity.Category;
import ksm.haein.item.entity.Item;
import ksm.haein.item.entity.ItemCategory;
import ksm.haein.item.entity.ItemPicture;
import ksm.haein.item.enums.CategoryName;
import ksm.haein.item.repository.CategoryRepository;
import ksm.haein.item.repository.ItemCategoryRepository;
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
                .price(request.price())
                .salePrice(request.salePrice())
                .shippingPrice(request.shippingPrice())
                .size(request.size())
                .color(request.color())
                .information(request.information())
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

        for (CategoryName categoryName : request.categories()) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));

            ItemCategory itemCategory = ItemCategory.builder()
                    .item(item)
                    .category(category)
                    .build();
            itemCategoryRepository.save(itemCategory);
        }

        return item.getId();
    }

    @Transactional
    public void updateItem(Long itemId, ItemUpdateRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + itemId));

        item.update(
                request.name(),
                request.price(),
                request.salePrice(),
                request.shippingPrice(),
                request.size(),
                request.color(),
                request.information()
        );

        if (request.pictureUrls() != null) {
            updateItemPictures(item, request.pictureUrls());
        }

        if (request.categories() != null) {
            updateItemCategories(item, request.categories());
        }

        itemRepository.save(item);
    }

    private void updateItemCategories(Item item, List<CategoryName> requestedCategories) {
        List<ItemCategory> existingCategories = itemCategoryRepository.findAllByItem(item);

        Set<CategoryName> existingNames = existingCategories.stream()
                .map(ic -> ic.getCategory().getName())
                .collect(Collectors.toSet());

        Set<CategoryName> toAdd = new HashSet<>(requestedCategories);
        toAdd.removeAll(existingNames);

        Set<CategoryName> toRemove = new HashSet<>(existingNames);
        toRemove.removeAll(requestedCategories);

        for (CategoryName categoryName : toRemove) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));
            itemCategoryRepository.deleteByItemAndCategory(item, category);
        }

        for (CategoryName categoryName : toAdd) {
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
}
