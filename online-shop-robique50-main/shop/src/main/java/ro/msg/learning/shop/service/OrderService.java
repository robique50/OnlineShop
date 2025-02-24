package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.dto.OrderProductDTO;
import ro.msg.learning.shop.dto.ProductLocationDTO;
import ro.msg.learning.shop.model.Keys.OrderDetailId;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.PlacedOrder;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.PlacedOrderRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.strategy.LocationStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final PlacedOrderRepository placedOrderRepository;

    private final StockRepository stockRepository;

    private final ProductRepository productRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final LocationRepository locationRepository;

    private final LocationStrategy locationStrategy;

    @Transactional
    public List<OrderDetail> createOrder(PlacedOrder placedOrder, List<OrderProductDTO> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }

        List<ProductLocationDTO> productLocations = locationStrategy.findLocationsForProducts(products);

        placedOrderRepository.save(placedOrder);

        updateStocks(products, productLocations);

        return saveOrderDetails(products, placedOrder, productLocations);
    }

    private void updateStocks(
            List<OrderProductDTO> products,
            List<ProductLocationDTO> productLocations) {

        List<Stock> currentStocks = stockRepository.findAllByProductIdAndLocationIdPairs(
                products.stream().map(OrderProductDTO::getProductId).toList(),
                productLocations.stream().map(ProductLocationDTO::getLocationId).toList()
        );

        Map<String, Stock> processedStocks = new HashMap<>();

        for (Stock currentStock : currentStocks) {
            String key = currentStock.getStockId().getProductId() + "-" + currentStock.getStockId().getLocationId();
            processedStocks.put(key, currentStock);
        }

        for (int i = 0; i < products.size(); i++) {
            OrderProductDTO product = products.get(i);
            ProductLocationDTO location = productLocations.get(i);
            String key = product.getProductId() + "-" + location.getLocationId();

            Stock currentStock = processedStocks.get(key);
            if (currentStock == null) {
                throw new IllegalStateException(
                        String.format("Stock not found for product %s at location %s",
                                product.getProductId(), location.getLocationId()));
            }

            int newQuantity = currentStock.getQuantity() - product.getQuantity();
            if (newQuantity < 0) {
                throw new IllegalStateException(
                        String.format("Insufficient stock for product %s. Required: %d, Available: %d",
                                product.getProductId(), product.getQuantity(), currentStock.getQuantity()));
            }

            currentStock.setQuantity(newQuantity);
        }
    }


    private List<OrderDetail> saveOrderDetails(
            List<OrderProductDTO> products,
            PlacedOrder placedOrder,
            List<ProductLocationDTO> productLocations) {

        Map<UUID, Location> locationCache = locationRepository.findAllById(
                        productLocations.stream()
                                .map(ProductLocationDTO::getLocationId)
                                .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Location::getId, location -> location));

        Map<UUID, Product> productCache = productRepository.findAllById(
                        products.stream()
                                .map(OrderProductDTO::getProductId)
                                .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            OrderProductDTO product = products.get(i);
            ProductLocationDTO locationInfo = productLocations.get(i);

            OrderDetail detail = OrderDetail.builder()
                    .orderDetailId(new OrderDetailId(placedOrder.getId(), product.getProductId()))
                    .product(productCache.get(product.getProductId()))
                    .order(placedOrder)
                    .quantity(product.getQuantity())
                    .shippedFrom(locationCache.get(locationInfo.getLocationId()))
                    .build();

            orderDetails.add(detail);
        }

        return orderDetailRepository.saveAll(orderDetails);
    }
}