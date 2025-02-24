package ro.msg.learning.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Entity
@Getter
@Setter
@Table(name = "product_category")
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    @Id
    private UUID id;

    private String name;

    private String description;
}