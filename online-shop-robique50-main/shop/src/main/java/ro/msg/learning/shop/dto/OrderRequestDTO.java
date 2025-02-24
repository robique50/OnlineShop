package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private UUID customerId;

    private LocalDateTime orderTimestamp;

    private String deliveryCountry;

    private String deliveryCity;

    private String deliveryStreetAddress;

    private List<OrderProductDTO> products;

}