package ksm.haein.item.repository;

import ksm.haein.item.entity.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    List<ItemOption> findAllByItemId(Long itemId);
    void deleteAllByItemId(Long itemId);
}
