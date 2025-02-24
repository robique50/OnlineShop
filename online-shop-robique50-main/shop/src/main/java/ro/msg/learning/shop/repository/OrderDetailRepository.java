package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.Keys.OrderDetailId;
import ro.msg.learning.shop.model.PlacedOrder;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    @Query("SELECT od FROM OrderDetail od WHERE od.order = :order")
    List<OrderDetail> findByOrder(PlacedOrder order);
}