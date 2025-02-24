package ro.msg.learning.shop.strategy;

import ro.msg.learning.shop.dto.OrderProductDTO;

import java.util.List;
import ro.msg.learning.shop.dto.ProductLocationDTO;

public interface LocationStrategy {
    List<ProductLocationDTO> findLocationsForProducts(List<OrderProductDTO> products);
}