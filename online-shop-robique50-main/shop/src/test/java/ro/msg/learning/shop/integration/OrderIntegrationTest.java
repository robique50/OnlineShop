package ro.msg.learning.shop.integration;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.dto.OrderProductDTO;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.User;
import ro.msg.learning.shop.model.PlacedOrder;
import ro.msg.learning.shop.model.Keys.StockId;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.UserRepository;
import ro.msg.learning.shop.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
class OrderIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    private Product product1;

    private Product product2;

    private Location location;

    private User customer;

    @BeforeEach
    void setUp() {
        customer = User.builder()
                .id(UUID.randomUUID())
                .firstName("Test")
                .lastName("Customer")
                .username("testcustomer")
                .password("password")
                .emailAddress("test@example.com")
                .userRole("CUSTOMER")
                .build();
        customer = userRepository.saveAndFlush(customer);

        product1 = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product 1")
                .description("Test Description 1")
                .price(BigDecimal.valueOf(10.00))
                .weight(1.0)
                .build();
        product1 = productRepository.saveAndFlush(product1);

        product2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product 2")
                .description("Test Description 2")
                .price(BigDecimal.valueOf(20.00))
                .weight(2.0)
                .build();
        product2 = productRepository.saveAndFlush(product2);

        location = Location.builder()
                .id(UUID.randomUUID())
                .name("Test Location")
                .country("Romania")
                .city("Cluj-Napoca")
                .streetAddress("Test Street 123")
                .build();
        location = locationRepository.saveAndFlush(location);

        Stock stock1 = Stock.builder()
                .stockId(new StockId(location.getId(), product1.getId()))
                .quantity(10)
                .build();
        Stock stock2 = Stock.builder()
                .stockId(new StockId(location.getId(), product2.getId()))
                .quantity(15)
                .build();
        stockRepository.saveAll(List.of(stock1, stock2));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Transactional
    void createOrder_WithSufficientStock_ShouldSucceed() {
        PlacedOrder order = PlacedOrder.builder()
                .customer(customer)
                .createdAt(LocalDateTime.now())
                .country("Romania")
                .city("Cluj-Napoca")
                .streetAddress("Test Street 123")
                .build();

        List<OrderProductDTO> orderProducts = List.of(
                new OrderProductDTO(product1.getId(), product1.getName(), 5)
        );

        orderService.createOrder(order, orderProducts);

        Stock updatedStock = stockRepository.findByProductIdAndLocationId(product1.getId(), location.getId())
                .orElseThrow(() -> new RuntimeException("Stock not found after order"));
        assertEquals(5, updatedStock.getQuantity(), "Stock quantity should be reduced by the ordered amount");
    }

    @Test
    @Transactional
    void createOrder_WithInsufficientStock_ShouldThrowException() {
        Stock insufficientStock = stockRepository.findByProductIdAndLocationId(product1.getId(), location.getId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        insufficientStock.setQuantity(3);
        stockRepository.saveAndFlush(insufficientStock);

        PlacedOrder order = PlacedOrder.builder()
                .customer(customer)
                .createdAt(LocalDateTime.now())
                .country("Romania")
                .city("Cluj-Napoca")
                .streetAddress("Test Street 123")
                .build();

        List<OrderProductDTO> orderProducts = List.of(
                new OrderProductDTO(product1.getId(), product1.getName(), 15)
        );

        assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(order, orderProducts), "Should throw IllegalArgumentException for insufficient stock");
    }

    @Test
    @Transactional
    void createOrder_WithEmptyProductList_ShouldThrowException() {
        PlacedOrder order = PlacedOrder.builder()
                .customer(customer)
                .createdAt(LocalDateTime.now())
                .country("Romania")
                .city("Cluj-Napoca")
                .streetAddress("Test Street 123")
                .build();

        assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(order, List.of()), "Should throw IllegalArgumentException for empty product list");
    }

    @Test
    @Transactional
    void createOrder_WithMultipleProducts_ShouldSucceed() {
        PlacedOrder order = PlacedOrder.builder()
                .customer(customer)
                .createdAt(LocalDateTime.now())
                .country("Romania")
                .city("Cluj-Napoca")
                .streetAddress("Test Street 123")
                .build();

        List<OrderProductDTO> orderProducts = List.of(
                new OrderProductDTO(product1.getId(), product1.getName(), 3),
                new OrderProductDTO(product2.getId(), product2.getName(), 4)
        );

        orderService.createOrder(order, orderProducts);

        Stock stock1 = stockRepository.findByProductIdAndLocationId(product1.getId(), location.getId())
                .orElseThrow(() -> new RuntimeException("Stock not found for product 1"));
        Stock stock2 = stockRepository.findByProductIdAndLocationId(product2.getId(), location.getId())
                .orElseThrow(() -> new RuntimeException("Stock not found for product 2"));

        assertEquals(7, stock1.getQuantity(), "First product stock should be reduced by 3");
        assertEquals(11, stock2.getQuantity(), "Second product stock should be reduced by 4");
    }
}
