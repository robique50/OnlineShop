package ro.msg.learning.shop.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import ro.msg.learning.shop.dto.OrderProductDTO;
import ro.msg.learning.shop.dto.ProductLocationDTO;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.Keys.StockId;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.UUID;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SingleLocationStrategyTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private SingleLocationStrategy strategy;

    private UUID locationId;

    private UUID product1Id;

    private UUID product2Id;

    @BeforeEach
    void setUp() {
        locationId = UUID.randomUUID();
        product1Id = UUID.randomUUID();
        product2Id = UUID.randomUUID();
    }

    @Test
    void whenSingleLocationHasAllProducts_thenReturnThatLocation() {
        List<OrderProductDTO> orderProducts = Arrays.asList(
                OrderProductDTO.builder()
                        .productId(product1Id)
                        .productName("Product 1")
                        .quantity(2)
                        .build(),
                OrderProductDTO.builder()
                        .productId(product2Id)
                        .productName("Product 2")
                        .quantity(3)
                        .build()
        );

        List<Stock> availableStocks = Arrays.asList(
                Stock.builder()
                        .stockId(new StockId(locationId, product1Id))
                        .quantity(5)
                        .build(),
                Stock.builder()
                        .stockId(new StockId(locationId, product2Id))
                        .quantity(5)
                        .build()
        );

        when(stockRepository.findStocksByProductIds(anyList())).thenReturn(availableStocks);

        List<ProductLocationDTO> result = strategy.findLocationsForProducts(orderProducts);

        assertEquals(2, result.size());

        result.forEach(dto -> assertEquals(locationId, dto.getLocationId(),
                "All products should come from the same location"));

        ProductLocationDTO firstProduct = result.stream()
                .filter(dto -> dto.getProductId().equals(product1Id))
                .findFirst()
                .orElseThrow();
        assertEquals(5, firstProduct.getAvailableQuantity());

        ProductLocationDTO secondProduct = result.stream()
                .filter(dto -> dto.getProductId().equals(product2Id))
                .findFirst()
                .orElseThrow();
        assertEquals(5, secondProduct.getAvailableQuantity());
    }

    @Test
    void whenNoLocationHasAllProducts_thenThrowException() {
        List<OrderProductDTO> orderProducts = Arrays.asList(
                OrderProductDTO.builder()
                        .productId(product1Id)
                        .productName("Product 1")
                        .quantity(5)
                        .build(),
                OrderProductDTO.builder()
                        .productId(product2Id)
                        .productName("Product 2")
                        .quantity(5)
                        .build()
        );

        List<Stock> availableStocks = Arrays.asList(
                Stock.builder()
                        .stockId(new StockId(locationId, product1Id))
                        .quantity(4)
                        .build(),
                Stock.builder()
                        .stockId(new StockId(locationId, product2Id))
                        .quantity(4)
                        .build()
        );

        when(stockRepository.findStocksByProductIds(anyList())).thenReturn(availableStocks);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.findLocationsForProducts(orderProducts));
    }
}