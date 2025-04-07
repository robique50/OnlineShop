package ro.msg.learning.shop.mapper;

import lombok.experimental.UtilityClass;
import ro.msg.learning.shop.dto.ProductDTO;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;

@UtilityClass
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .weight(product.getWeight())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .imageUrl(product.getImageUrl())
                .build();
    }

    public Product toEntity(ProductDTO dto, ProductCategory category) {
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .weight(dto.getWeight())
                .category(category)
                .imageUrl(dto.getImageUrl())
                .build();
    }
}