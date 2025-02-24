package ro.msg.learning.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private Double weight;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    private String supplier;

    private String imageUrl;
}