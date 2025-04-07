package ro.msg.learning.shop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ProductDTO {
    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private Double weight;

    private UUID categoryId;

    private String categoryName;

    private String imageUrl;
}