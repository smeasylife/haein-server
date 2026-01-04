package ksm.haein.item.repository;

import ksm.haein.item.entity.Category;
import ksm.haein.item.entity.Item;
import ksm.haein.item.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    List<ItemCategory> findAllByItem(Item item);

    @Transactional
    void deleteByItemId(Long itemId);

    @Transactional
    void deleteByItemAndCategory(Item item, Category category);
}
