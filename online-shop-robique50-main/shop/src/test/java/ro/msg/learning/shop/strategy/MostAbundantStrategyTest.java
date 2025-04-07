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

import java.util.Arrays;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MostAbundantStrategyTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private MostAbundantStrategy strategy;

    private UUID location1Id;

    private UUID location2Id;

    private UUID product1Id;

    private UUID product2Id;

    @BeforeEach
    void setUp() {
        location1Id = UUID.randomUUID();
        location2Id = UUID.randomUUID();
        product1Id = UUID.randomUUID();
        product2Id = UUID.randomUUID();
    }

    @Test
    void whenProductsAvailableInMultipleLocations_thenSelectMostAbundant() {
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
                        .stockId(new StockId(location1Id, product1Id))
                        .quantity(10)
                        .build(),
                Stock.builder()
                        .stockId(new StockId(location2Id, product1Id))
                        .quantity(5)
                        .build(),
                Stock.builder()
                        .stockId(new StockId(location1Id, product2Id))
                        .quantity(5)
                        .build(),
                Stock.builder()
                        .stockId(new StockId(location2Id, product2Id))
                        .quantity(15)
                        .build()
        );

        when(stockRepository.findStocksByProductIds(anyList())).thenReturn(availableStocks);

        List<ProductLocationDTO> result = strategy.findLocationsForProducts(orderProducts);

        assertEquals(2, result.size());

        ProductLocationDTO firstProduct = result.stream()
                .filter(dto -> dto.getProductId().equals(product1Id))
                .findFirst()
                .orElseThrow();
        assertEquals(location1Id, firstProduct.getLocationId());
        assertEquals(10, firstProduct.getAvailableQuantity());

        ProductLocationDTO secondProduct = result.stream()
                .filter(dto -> dto.getProductId().equals(product2Id))
                .findFirst()
                .orElseThrow();
        assertEquals(location2Id, secondProduct.getLocationId());
        assertEquals(15, secondProduct.getAvailableQuantity());

        assertNotEquals(firstProduct.getLocationId(), secondProduct.getLocationId(),
                "Products should come from different locations");
    }

    @Test
    void whenInsufficientTotalStock_thenThrowException() {
        List<OrderProductDTO> orderProducts = List.of(
                OrderProductDTO.builder()
                        .productId(product1Id)
                        .productName("Product 1")
                        .quantity(20)
                        .build()
        );

        List<Stock> availableStocks = Arrays.asList(
                Stock.builder()
                        .stockId(new StockId(location1Id, product1Id))
                        .quantity(10)
                        .build(),
                Stock.builder()
                        .stockId(new StockId(location2Id, product1Id))
                        .quantity(5)
                        .build()
        );

        when(stockRepository.findStocksByProductIds(anyList())).thenReturn(availableStocks);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.findLocationsForProducts(orderProducts));
    }
}