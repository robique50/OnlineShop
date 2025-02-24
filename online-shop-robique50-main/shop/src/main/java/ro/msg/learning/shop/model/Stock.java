package ro.msg.learning.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.msg.learning.shop.model.Keys.StockId;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stock")
public class Stock {
    @EmbeddedId
    private StockId stockId;

    @Column(nullable = false)
    private Integer quantity;
}