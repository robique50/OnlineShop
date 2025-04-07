package ro.msg.learning.shop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    private ProductCategory testCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID categoryId = UUID.randomUUID();
        testCategory = ProductCategory.builder()
                .id(categoryId)
                .name("Test Category")
                .build();

        testProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Description")
                .price(BigDecimal.valueOf(100))
                .weight(1.5)
                .category(testCategory)
                .build();
    }

    @Test
    void testFindAll() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        var products = productService.findAll();

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        Product foundProduct = productService.findById(testProduct.getId());

        assertNotNull(foundProduct);
        assertEquals(testProduct.getId(), foundProduct.getId());
        verify(productRepository, times(1)).findById(testProduct.getId());
    }

    @Test
    void testSave() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product savedProduct = productService.save(testProduct);

        assertNotNull(savedProduct);
        assertEquals(testProduct.getName(), savedProduct.getName());
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void testDeleteById() {
        doNothing().when(productRepository).deleteById(testProduct.getId());

        productService.deleteById(testProduct.getId());

        verify(productRepository, times(1)).deleteById(testProduct.getId());
    }

    @Test
    void testFindCategoryByName() {
        when(productCategoryRepository.findByName(testCategory.getName())).thenReturn(Optional.of(testCategory));

        ProductCategory foundCategory = productService.findCategoryByName(testCategory.getName());

        assertNotNull(foundCategory);
        assertEquals(testCategory.getName(), foundCategory.getName());
        verify(productCategoryRepository, times(1)).findByName(testCategory.getName());
    }
}