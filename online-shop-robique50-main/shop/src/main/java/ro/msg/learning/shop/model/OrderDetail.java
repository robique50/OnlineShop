package ro.msg.learning.shop.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import ro.msg.learning.shop.model.Keys.OrderDetailId;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_detail")
public class OrderDetail {
    @EmbeddedId
    private OrderDetailId orderDetailId;

    private Integer quantity;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private PlacedOrder order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "shipped_from", nullable = false)
    private Location shippedFrom;
}