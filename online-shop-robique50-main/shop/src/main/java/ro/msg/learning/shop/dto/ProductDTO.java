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
    private UUID productId;

    private String productName;

    private String productDescription;

    private BigDecimal productPrice;

    private Double productWeight;

    private UUID categoryId;

    private String categoryName;
}