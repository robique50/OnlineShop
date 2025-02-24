package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.model.PlacedOrder;

import java.util.UUID;

public interface PlacedOrderRepository extends JpaRepository<PlacedOrder, UUID> {
}