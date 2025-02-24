package ro.msg.learning.shop.mapper;

import lombok.experimental.UtilityClass;
import ro.msg.learning.shop.dto.ProductDTO;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;

@UtilityClass
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .productPrice(product.getPrice())
                .productWeight(product.getWeight())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public Product toEntity(ProductDTO dto, ProductCategory category) {
        return Product.builder()
                .id(dto.getProductId())
                .name(dto.getProductName())
                .description(dto.getProductDescription())
                .price(dto.getProductPrice())
                .weight(dto.getProductWeight())
                .category(category)
                .build();
    }
}