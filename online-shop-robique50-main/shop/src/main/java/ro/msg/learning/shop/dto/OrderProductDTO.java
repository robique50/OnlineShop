package ro.msg.learning.shop.dto;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class OrderProductDTO {
    private UUID productId;

    private String productName;

    private int quantity;
}