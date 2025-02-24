package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.OrderProductDTO;
import ro.msg.learning.shop.dto.ProductLocationDTO;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MostAbundantStrategy implements LocationStrategy {
    private final StockRepository stockRepository;

    @Override
    public List<ProductLocationDTO> findLocationsForProducts(List<OrderProductDTO> products) {
        List<UUID> productIds = products.stream()
                .map(OrderProductDTO::getProductId)
                .toList();

        List<Stock> allStocks = stockRepository.findStocksByProductIds(productIds);

        verifyTotalStockAvailability(products, allStocks);

        List<ProductLocationDTO> result = new ArrayList<>();

        for (OrderProductDTO product : products) {
            ProductLocationDTO allocation = findMostAbundantLocationForProduct(product, allStocks);
            result.add(allocation);
        }

        return result;
    }

    private void verifyTotalStockAvailability(List<OrderProductDTO> products, List<Stock> allStocks) {
        Map<UUID, Integer> requiredQuantities = products.stream()
                .collect(Collectors.toMap(
                        OrderProductDTO::getProductId,
                        OrderProductDTO::getQuantity
                ));

        Map<UUID, Integer> totalStockPerProduct = allStocks.stream()
                .collect(Collectors.groupingBy(
                        stock -> stock.getStockId().getProductId(),
                        Collectors.summingInt(Stock::getQuantity)
                ));

        requiredQuantities.forEach((productId, requiredQuantity) -> {
            int availableTotal = totalStockPerProduct.getOrDefault(productId, 0);
            if (availableTotal < requiredQuantity) {
                throw new IllegalArgumentException(
                        String.format("Insufficient total stock for product %s. Required: %d, Available: %d",
                                productId, requiredQuantity, availableTotal));
            }
        });
    }

    private ProductLocationDTO findMostAbundantLocationForProduct(
            OrderProductDTO product,
            List<Stock> allStocks) {

        List<Stock> productStocks = allStocks.stream()
                .filter(stock -> stock.getStockId().getProductId().equals(product.getProductId()))
                .toList();

        Stock selectedStock = productStocks.stream()
                .filter(stock -> stock.getQuantity() >= product.getQuantity())
                .max(Comparator.comparing(Stock::getQuantity))
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("No location has sufficient stock for product %s. Required: %d",
                                product.getProductId(), product.getQuantity())));

        return ProductLocationDTO.builder()
                .productId(product.getProductId())
                .locationId(selectedStock.getStockId().getLocationId())
                .availableQuantity(selectedStock.getQuantity())
                .build();
    }
}