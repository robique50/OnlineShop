package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.msg.learning.shop.model.ProductCategory;

import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.name = :name")
    Optional<ProductCategory> findByName(@Param("name") String name);
}