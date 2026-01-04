package ksm.haein.item.repository;

import ksm.haein.item.entity.Item;
import ksm.haein.item.entity.ItemPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemPictureRepository extends JpaRepository<ItemPicture, Long> {
    List<ItemPicture> findAllByItem(Item item);

    @Transactional
    void deleteByItemId(Long itemId);

    @Transactional
    void deleteByItemAndUrl(Item item, String url);
}
