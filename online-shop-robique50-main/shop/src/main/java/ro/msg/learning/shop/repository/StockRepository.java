package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.Keys.StockId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, StockId> {

    @Query("SELECT s FROM Stock s WHERE s.stockId.productId IN :productIds")
    List<Stock> findStocksByProductIds(@Param("productIds") List<UUID> productIds);

    @Query("SELECT s FROM Stock s WHERE s.stockId.productId = :productId AND s.stockId.locationId = :locationId")
    Optional<Stock> findByProductIdAndLocationId(
            @Param("productId") UUID productId,
            @Param("locationId") UUID locationId);

    @Query("SELECT s FROM Stock s WHERE s.stockId.productId IN :productIds AND s.stockId.locationId IN :locationIds")
    List<Stock> findAllByProductIdAndLocationIdPairs(
            @Param("productIds") List<UUID> productIds,
            @Param("locationIds") List<UUID> locationIds);
}