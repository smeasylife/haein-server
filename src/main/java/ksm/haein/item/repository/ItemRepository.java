package ksm.haein.item.repository;

import ksm.haein.item.dto.ItemData;
import ksm.haein.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT new ksm.haein.item.dto.ItemData(i.id, i.name, i.price, i.salePrice, i.color, p.url)" +
            "FROM Item i " +
            "LEFT JOIN i.pictures p ON p.id = (SELECT MIN(p2.id) FROM ItemPicture p2 WHERE p2.item = i)")
    Page<ItemData> findItemByPage(Pageable pageable);
}
