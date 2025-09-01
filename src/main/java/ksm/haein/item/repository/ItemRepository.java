package ksm.haein.item.repository;

import ksm.haein.item.dto.ItemData;
import ksm.haein.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT new ksm.haein.item.dto.ItemData(i.id, i.name, i.price, i.salePrice, i.color, p.url, FALSE)" +
            "FROM Item i " +
            "LEFT JOIN i.pictures p ON p.id = (SELECT MIN(p2.id) FROM ItemPicture p2 WHERE p2.item = i)")
    Page<ItemData> findItemByPageWithoutLike(Pageable pageable);

    @Query("SELECT new ksm.haein.item.dto.ItemData(i.id, i.name, i.price, i.salePrice, i.color, p.url, CASE WHEN l.id IS NOT NULL THEN TRUE ELSE FALSE END)" +
            "FROM Item i " +
            "LEFT JOIN i.pictures p ON p.id = (SELECT MIN(p2.id) FROM ItemPicture p2 WHERE p2.item = i)" +
            "LEFT JOIN Like l ON l.item = i AND l.member.id = :memberId")
    Page<ItemData> findItemByPageWithLike(Pageable pageable, Long memberId);

    @Query("SELECT i FROM Item i " +
           "LEFT JOIN FETCH i.pictures " +
           "LEFT JOIN FETCH i.reviews " +
           "LEFT JOIN FETCH i.questions " +
           "WHERE i.id = :id")
    Optional<Item> findItemWithDetailsById(@Param("id") Long id);
}
