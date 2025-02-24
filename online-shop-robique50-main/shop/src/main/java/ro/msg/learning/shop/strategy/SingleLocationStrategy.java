package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.OrderProductDTO;
import ro.msg.learning.shop.dto.ProductLocationDTO;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SingleLocationStrategy implements LocationStrategy {
    private final StockRepository stockRepository;

    @Override
    public List<ProductLocationDTO> findLocationsForProducts(List<OrderProductDTO> products) {
        List<UUID> productIds = products.stream()
                .map(OrderProductDTO::getProductId)
                .toList();

        Map<UUID, Integer> requiredQuantities = products.stream()
                .collect(Collectors.toMap(
                        OrderProductDTO::getProductId,
                        OrderProductDTO::getQuantity
                ));

        List<Stock> allStocks = stockRepository.findStocksByProductIds(productIds);

        Map<UUID, List<Stock>> stocksByLocation = allStocks.stream()
                .collect(Collectors.groupingBy(stock -> stock.getStockId().getLocationId()));

        UUID suitableLocationId = stocksByLocation.entrySet().stream()
                .filter(entry -> canLocationFulfillOrder(entry.getValue(), requiredQuantities))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No single location has sufficient stock for all products"));

        return products.stream()
                .map(product -> {
                    Stock stockForProduct = allStocks.stream()
                            .filter(stock -> stock.getStockId().getLocationId().equals(suitableLocationId)
                                    && stock.getStockId().getProductId().equals(product.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException(
                                    "Stock not found for product: " + product.getProductId()));

                    return ProductLocationDTO.builder()
                            .productId(product.getProductId())
                            .locationId(suitableLocationId)
                            .availableQuantity(stockForProduct.getQuantity())
                            .build();
                })
                .toList();
    }

    private boolean canLocationFulfillOrder(List<Stock> locationStocks, Map<UUID, Integer> requiredQuantities) {
        Map<UUID, Integer> availableQuantities = locationStocks.stream()
                .collect(Collectors.toMap(
                        stock -> stock.getStockId().getProductId(),
                        Stock::getQuantity
                ));

        return requiredQuantities.entrySet().stream()
                .allMatch(entry -> {
                    Integer availableQuantity = availableQuantities.get(entry.getKey());
                    return availableQuantity != null && availableQuantity >= entry.getValue();
                });
    }
}