package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.ConvenienceType;
import com.blueDragon.Convenience.Model.FoodType;
import com.blueDragon.Convenience.Model.Product;
import com.blueDragon.Convenience.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceTest {
    @Autowired
    private CUProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        // Mock the save operation to return the product passed to it
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void testCreateProductWithValidPrefix() {
        // 유효한 이름 예시
        Product product1 = productService.createProduct("주)후식곰창볶음주먹밥", 4500, List.of(ConvenienceType.CU));
        assertNotNull(product1); // Ensure the product is not null
        assertEquals("후식곰창볶음주먹밥", product1.getName());
        assertEquals(FoodType.밥류, product1.getFoodTypes().get(0));

        Product product2 = productService.createProduct("면)압도적소시파스타", 3500, List.of(ConvenienceType.CU));
        assertNotNull(product2); // Ensure the product is not null
        assertEquals("압도적소시파스타", product2.getName());
        assertEquals(FoodType.면류, product2.getFoodTypes().get(0));

        Product product3 = productService.createProduct("샐)풀드포크고기샐러드", 5000, List.of(ConvenienceType.CU));
        assertNotNull(product3); // Ensure the product is not null
        assertEquals("풀드포크고기샐러드", product3.getName());
        assertEquals(FoodType.다이어트류, product3.getFoodTypes().get(0));
    }

    @Test
    public void testCreateProductWithSaladPrefix() {
        // "샐)" 접두사와 "치킨" 키워드가 포함된 상품명 테스트
        Product product = productService.createProduct("샐)예지력치킨텐더샐러드", 3000, List.of(ConvenienceType.CU));

        // Ensure the product is not null and check the expected values
        assertNotNull(product);
        assertEquals("예지력치킨텐더샐러드", product.getName());
        assertEquals(FoodType.다이어트류, product.getFoodTypes().get(0));
    }

}