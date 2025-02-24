package ro.msg.learning.shop.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.OrderRequestDTO;
import ro.msg.learning.shop.dto.OrderResponseDTO;
import ro.msg.learning.shop.dto.OrderProductDTO;
import ro.msg.learning.shop.model.PlacedOrder;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.User;
import ro.msg.learning.shop.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final UserRepository userRepository;

    public PlacedOrder toEntity(OrderRequestDTO dto) {
        if (dto.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID must not be null");
        }

        User customer = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found for id: " + dto.getCustomerId()));

        return PlacedOrder.builder()
                .customer(customer)
                .createdAt(dto.getOrderTimestamp() != null ? dto.getOrderTimestamp() : LocalDateTime.now())
                .country(dto.getDeliveryCountry())
                .city(dto.getDeliveryCity())
                .streetAddress(dto.getDeliveryStreetAddress())
                .build();
    }

    public OrderResponseDTO toDTO(PlacedOrder order, List<OrderDetail> orderDetails) {
        List<OrderProductDTO> products = orderDetails.stream()
                .map(detail -> OrderProductDTO.builder()
                        .productId(detail.getProduct().getId())
                        .productName(detail.getProduct().getName())
                        .quantity(detail.getQuantity())
                        .build())
                .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .orderTimestamp(order.getCreatedAt())
                .deliveryCountry(order.getCountry())
                .deliveryCity(order.getCity())
                .deliveryStreetAddress(order.getStreetAddress())
                .products(products)
                .build();
    }
}