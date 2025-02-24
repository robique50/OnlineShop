package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.OrderRequestDTO;
import ro.msg.learning.shop.dto.OrderResponseDTO;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.PlacedOrder;
import ro.msg.learning.shop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    private final OrderMapper orderMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        PlacedOrder orderToCreate = orderMapper.toEntity(orderRequest);

        List<OrderDetail> orderDetails = orderService.createOrder(
                orderToCreate,
                orderRequest.getProducts()
        );

        OrderResponseDTO response = orderMapper.toDTO(orderToCreate, orderDetails);

        return ResponseEntity.ok(response);
    }
}