package ro.msg.learning.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private UUID orderId;

    private LocalDateTime orderTimestamp;

    private String deliveryCountry;

    private String deliveryCity;

    private String deliveryStreetAddress;

    private List<OrderProductDTO> products;
}