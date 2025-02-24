package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductLocationDTO {
    private UUID productId;

    private UUID locationId;

    private Integer availableQuantity;
}