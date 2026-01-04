package ksm.haein.item.repository;

import ksm.haein.item.entity.Category;
import ksm.haein.item.enums.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(CategoryName name);
}
