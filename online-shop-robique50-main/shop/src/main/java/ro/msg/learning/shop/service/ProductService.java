package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

    public ProductCategory findCategoryByName(String name) {
        return productCategoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product category not found with name: " + name));
    }
}